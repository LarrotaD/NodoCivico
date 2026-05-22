package com.tuequipo.nodocivico.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tuequipo.nodocivico.data.local.AppDatabase
import com.tuequipo.nodocivico.data.model.Report
import com.tuequipo.nodocivico.data.repository.ReportRepository
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReportRepository
    val allReports: LiveData<List<Report>>

    init {
        val reportDao = AppDatabase.getDatabase(application).reportDao()
        repository = ReportRepository(reportDao)
        allReports = repository.allReports.asLiveData()
    }

    fun insert(report: Report) = viewModelScope.launch {
        repository.insert(report)
    }

    fun update(report: Report) = viewModelScope.launch {
        repository.update(report)
    }

    fun delete(report: Report) = viewModelScope.launch {
        repository.delete(report)
    }
}