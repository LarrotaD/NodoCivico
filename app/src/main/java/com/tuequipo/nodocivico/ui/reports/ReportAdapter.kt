package com.tuequipo.nodocivico.ui.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tuequipo.nodocivico.R
import com.tuequipo.nodocivico.data.model.Report

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reportsList = emptyList<Report>()

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Asumiendo nombres estándar temporales basados en los datos
        val tvTitle: TextView = itemView.findViewById(android.R.id.text1)
        val tvDescription: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        // Usamos un layout simple del sistema por defecto para no romper tus recursos visuales actuales
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport = reportsList[position]
        holder.tvTitle.text = "${currentReport.title} [${currentReport.priority}]"
        holder.tvDescription.text = "${currentReport.category} - ${currentReport.location}"

        // ==========================================
        // CLIC CORTO: Navegar a la pantalla de DETALLE
        // ==========================================
        holder.itemView.setOnClickListener {
            // Guardamos el ID del reporte en un paquete (Bundle) para que el Detalle sepa cuál cargar
            val bundle = android.os.Bundle().apply {
                putInt("reportId", currentReport.id)
            }

            // Ejecutamos la acción de navegación hacia el fragmento de detalle
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
                    // Buscamos el ViewModel y borramos de Room de manera offline
                    val viewModel = androidx.lifecycle.ViewModelProvider(context as androidx.appcompat.app.AppCompatActivity)[ReportViewModel::class.java]
                    viewModel.delete(currentReport)
                    android.widget.Toast.makeText(context, "Reporte eliminado de forma offline", android.widget.Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
            true // Indica que procesamos el clic largo correctamente
        }
    }

    override fun getItemCount(): Int = reportsList.size

    fun setReports(reports: List<Report>) {
        this.reportsList = reports
        notifyDataSetChanged()
    }


}