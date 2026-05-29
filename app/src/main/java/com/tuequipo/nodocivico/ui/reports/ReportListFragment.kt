package com.tuequipo.nodocivico.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuequipo.nodocivico.R

class ReportListFragment : Fragment() {

    private lateinit var reportViewModel: ReportViewModel
    private lateinit var adapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CORREGIDO: Buscamos exactamente el ID del componente del nuevo XML
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerReports)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        val progressLoading = view.findViewById<ProgressBar>(R.id.progressLoading)
        val tvConnectionBanner = view.findViewById<TextView>(R.id.tvConnectionBanner)

        // 1. Configurar el RecyclerView y su LayoutManager
        adapter = ReportAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // 2. Activar el estado de carga visual antes de leer los datos locales
        progressLoading.visibility = View.VISIBLE
        recycler.visibility = View.GONE
        tvEmpty.visibility = View.GONE

        // 3. Inicializar el ViewModel de Room
        reportViewModel = ViewModelProvider(this)[ReportViewModel::class.java]

        // 4. Observar el flujo de datos (LiveData/Flow) en tiempo real
        reportViewModel.allReports.observe(viewLifecycleOwner) { reports ->
            // Ocultar la barra de carga cuando Room responda
            progressLoading.visibility = View.GONE

            if (reports.isNullOrEmpty()) {
                // ESTADO VACÍO: Si no hay registros en la base de datos local
                recycler.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            } else {
                // ESTADO CON DATOS REALES: Mostrar la lista y enviarle los reportes al adaptador
                tvEmpty.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                adapter.setReports(reports)
            }
        }

        // Validar conectividad actual al abrir la pantalla
        if (isInternetAvailable(requireContext())) {
            tvConnectionBanner.text = "Conectado al servidor - Datos actualizados"
            tvConnectionBanner.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50")) // Verde
        } else {
            tvConnectionBanner.text = "Sin conexión - Modo Offline activo"
            tvConnectionBanner.setBackgroundColor(android.graphics.Color.parseColor("#F44336")) // Rojo
        }
    }

    private fun isInternetAvailable(context: android.content.Context): Boolean {
        val connectivityManager = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}