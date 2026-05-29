package com.tuequipo.nodocivico.ui.reports

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tuequipo.nodocivico.R
import com.tuequipo.nodocivico.data.local.AppDatabase
import com.tuequipo.nodocivico.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvProfileName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvProfileNeighborhood = view.findViewById<TextView>(R.id.tvProfileNeighborhood)
        val btnForceSync = view.findViewById<Button>(R.id.btnForceSync)

        // 1. LEER SHAREDPREFERENCES: Cargar los datos guardados en el login
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Ciudadano Anónimo")
        val neighborhood = sharedPreferences.getString("neighborhood", "No asignado")

        tvProfileName.text = "Vecino: $username"
        tvProfileNeighborhood.text = "Barrio: $neighborhood"

        // 2. LOGICA DE SINCRONIZACIÓN MANUAL (Consumo de la API REST)
        btnForceSync.setOnClickListener {
            if (isInternetAvailable(requireContext())) {
                btnForceSync.isEnabled = false
                btnForceSync.text = "Sincronizando..."

                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    val unsyncedReports = db.reportDao().getUnsyncedReports()

                    if (unsyncedReports.isNotEmpty()) {
                        var successCount = 0
                        for (report in unsyncedReports) {
                            try {
                                // Consumo real de la API REST
                                RetrofitClient.apiService.uploadReport(report)
                                db.reportDao().updateReport(report.copy(isSynced = true))
                                successCount++
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Se subieron $successCount reportes con éxito", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Todos los reportes ya estaban actualizados", Toast.LENGTH_SHORT).show()
                        }
                    }

                    withContext(Dispatchers.Main) {
                        btnForceSync.isEnabled = true
                        btnForceSync.text = "Sincronizar Reportes Manualmente"
                    }
                }
            } else {
                Toast.makeText(context, "Error: No tienes conexión a internet en este momento", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}