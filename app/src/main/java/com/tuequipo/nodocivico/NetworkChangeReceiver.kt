package com.tuequipo.nodocivico.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.tuequipo.nodocivico.data.local.AppDatabase
import com.tuequipo.nodocivico.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (isInternetAvailable(context)) {
            Log.d("NetworkChangeReceiver", "¡Conexión a internet restaurada! Iniciando sincronización...")

            // Disparamos una corrutina global en segundo plano para procesar la subida
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val unsyncedReports = db.reportDao().getUnsyncedReports()

                if (unsyncedReports.isNotEmpty()) {
                    var successCount = 0
                    for (report in unsyncedReports) {
                        try {
                            // CONSUMO REAL API REST: Enviamos el reporte local al servidor
                            RetrofitClient.apiService.uploadReport(report)

                            // Si el servidor responde con éxito, actualizamos en Room a isSynced = true
                            val syncedReport = report.copy(isSynced = true)
                            db.reportDao().updateReport(syncedReport)
                            successCount++
                        } catch (e: Exception) {
                            Log.e("NetworkChangeReceiver", "Error al sincronizar reporte ID ${report.id}: ${e.message}")
                        }
                    }

                    if (successCount > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Sincronizados $successCount reportes pendientes con el servidor", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else {
            Log.d("NetworkChangeReceiver", "Dispositivo sin conexión a internet. Modo Offline activo.")
        }
    }

    // Función auxiliar para verificar el estado real de la red
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}