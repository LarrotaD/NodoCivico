package com.tuequipo.nodocivico.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuequipo.nodocivico.R
import com.tuequipo.nodocivico.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportDetailFragment : Fragment() {

    private var reportId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportId = arguments?.getInt("reportId", -1) ?: -1

        val tvTitle = view.findViewById<TextView>(R.id.tvDetailTitle)
        val tvCategory = view.findViewById<TextView>(R.id.tvDetailCategory)
        val tvLocation = view.findViewById<TextView>(R.id.tvDetailLocation)
        val tvStatus = view.findViewById<TextView>(R.id.tvDetailStatus)
        val tvDescription = view.findViewById<TextView>(R.id.tvDetailDescription)
        val btnGoToEdit = view.findViewById<Button>(R.id.btnGoToEdit)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val report = withContext(Dispatchers.IO) { db.reportDao().getReportById(reportId) }
            report?.let {
                tvTitle.text = it.title
                tvCategory.text = "Categoría: ${it.category}"
                tvLocation.text = "Ubicación: ${it.location}"
                tvStatus.text = "Estado: ${it.status}"
                tvDescription.text = it.description
            }
        }

        btnGoToEdit.setOnClickListener {
            val bundle = Bundle().apply { putInt("reportId", reportId) }
            findNavController().navigate(R.id.createReportFragment, bundle)
        }
    }
}