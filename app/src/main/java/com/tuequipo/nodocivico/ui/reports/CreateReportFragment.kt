package com.tuequipo.nodocivico.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuequipo.nodocivico.R
import com.tuequipo.nodocivico.data.local.AppDatabase
import com.tuequipo.nodocivico.data.model.Report
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateReportFragment : Fragment() {

    private lateinit var reportViewModel: ReportViewModel
    private var isEditMode = false
    private var reportIdToEdit = -1
    private var existingReportStatus = "PENDIENTE"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel = ViewModelProvider(this)[ReportViewModel::class.java]

        val tvFormTitle = view.findViewById<TextView>(R.id.tvFormTitle)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etCategory = view.findViewById<EditText>(R.id.etCategory)
        val etLocation = view.findViewById<EditText>(R.id.etLocation)
        val btnSaveReport = view.findViewById<Button>(R.id.btnSaveReport)

        // Recuperar argumentos para verificar si es Modo Edición
        arguments?.let {
            reportIdToEdit = it.getInt("reportId", -1)
            if (reportIdToEdit != -1) {
                isEditMode = true
                tvFormTitle.text = "Editar Reporte"
                btnSaveReport.text = "Actualizar Cambios"

                // Cargar datos actuales desde Room usando hilos de fondo coroutines
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    val report = withContext(Dispatchers.IO) {
                        db.reportDao().getReportById(reportIdToEdit)
                    }
                    report?.let {
                        etTitle.setText(it.title)
                        etDescription.setText(it.description)
                        etCategory.setText(it.category)
                        etLocation.setText(it.location)
                        existingReportStatus = it.status
                    }
                }
            }
        }

        btnSaveReport.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val category = etCategory.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (title.isEmpty() || description.isEmpty() || category.isEmpty() || location.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                // OPERACIÓN: UPDATE (Actualizar)
                val updatedReport = Report(
                    id = reportIdToEdit,
                    title = title,
                    description = description,
                    category = category,
                    priority = "ALTA",
                    location = location,
                    status = existingReportStatus,
                    createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                reportViewModel.update(updatedReport)
                Toast.makeText(requireContext(), "Reporte actualizado localmente", Toast.LENGTH_SHORT).show()
            } else {
                // OPERACIÓN: CREATE (Insertar)
                val newReport = Report(
                    title = title,
                    description = description,
                    category = category,
                    priority = "ALTA",
                    location = location,
                    status = "PENDIENTE",
                    createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                reportViewModel.insert(newReport)
                Toast.makeText(requireContext(), "Reporte guardado offline", Toast.LENGTH_SHORT).show()
            }

            // Regresar automáticamente a la pantalla anterior
            findNavController().popBackStack()
        }
    }
}