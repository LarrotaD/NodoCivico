package com.tuequipo.nodocivico.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootReceiver", "El sistema operativo Android ha iniciado correctamente.")
            // Aquí se reactivan los chequeos o alarmas de fondo del sistema
            Toast.makeText(context, "Nodo Cívico: Servicio de monitoreo local reactivado", Toast.LENGTH_SHORT).show()
        }
    }
}