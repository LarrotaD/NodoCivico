package com.tuequipo.nodocivico.ui.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tuequipo.nodocivico.R
import com.tuequipo.nodocivico.data.model.Report

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reportsList = emptyList<Report>()

    // El ViewHolder ahora apunta a los componentes reales de tu item_report.xml
    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvReportTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvReportDescription)
        val ivSyncStatus: ImageView = itemView.findViewById(R.id.ivSyncStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        // CORREGIDO: Inflamos tu nuevo archivo de diseño creado desde cero
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport = reportsList[position]

        // Asignamos la información a los textos
        holder.tvTitle.text = "${currentReport.title} [${currentReport.priority}]"
        holder.tvDescription.text = "${currentReport.category} - ${currentReport.location}"

        // ======================================================================
        // INDICADORES DE SINCRONIZACIÓN: Cambiar ícono dinámicamente según la API
        // ======================================================================
        if (currentReport.isSynced) {
            // Si ya está en el servidor: Nube/Check verde
            holder.ivSyncStatus.setImageResource(android.R.drawable.checkbox_on_background)
            holder.ivSyncStatus.setColorFilter(android.graphics.Color.parseColor("#4CAF50")) // Verde
        } else {
            // Si solo está guardado localmente: Flecha de subida/Alerta naranja
            holder.ivSyncStatus.setImageResource(android.R.drawable.stat_sys_upload)
            holder.ivSyncStatus.setColorFilter(android.graphics.Color.parseColor("#FF9800")) // Naranja
        }

        // ==========================================
        // CLIC CORTO: Navegar a la pantalla de DETALLE
        // ==========================================
        holder.itemView.setOnClickListener {
            val bundle = android.os.Bundle().apply {
                putInt("reportId", currentReport.id)
            }
            androidx.navigation.Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_list_to_detail, bundle)
        }

        // ==========================================
        // CLIC LARGO: Alerta para ELIMINAR el reporte (CRUD - Delete)
        // ==========================================
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            android.app.AlertDialog.Builder(context)
                .setTitle("Eliminar reporte")
                .setMessage("¿Deseas borrar este reporte de la base de datos local?")
                .setPositiveButton("Sí") { _, _ ->
                    val viewModel = androidx.lifecycle.ViewModelProvider(
                        context as androidx.appcompat.app.AppCompatActivity
                    )[ReportViewModel::class.java]
                    viewModel.delete(currentReport)
                    android.widget.Toast.makeText(context, "Reporte eliminado de forma offline", android.widget.Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount(): Int = reportsList.size

    fun setReports(reports: List<Report>) {
        this.reportsList = reports
        notifyDataSetChanged()
    }
}