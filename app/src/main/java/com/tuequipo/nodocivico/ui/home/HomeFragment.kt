package com.tuequipo.nodocivico.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tuequipo.nodocivico.R
import com.tuequipo.nodocivico.ui.reports.ReportViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // Dentro de HomeFragment.kt -> onViewCreated:
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        val btnVerReportes = view.findViewById<Button>(R.id.btnVerReportes)
        val btnNuevoReporte = view.findViewById<Button>(R.id.btnNuevoReporte)

        // Recuperar datos offline
        val sharedPref = requireActivity().getSharedPreferences("NodoCivicoPrefs", android.content.Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "vecino")
        tvGreeting.text = "¡Hola, $userName!"

        btnVerReportes.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_reportList)
        }

        // Opcional para simular inserciones offline en este entregable:
// Reemplaza el setOnClickListener de btnNuevoReporte para que quede así:
        btnNuevoReporte.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_createReport)
        }
    }
}