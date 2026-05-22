package com.tuequipo.nodocivico.data.repository

import com.tuequipo.nodocivico.data.local.dao.ReportDao
import com.tuequipo.nodocivico.data.model.Report
import kotlinx.coroutines.flow.Flow

class ReportRepository(private val reportDao: ReportDao) {
    val allReports: Flow<List<Report>> = reportDao.getAllReports()

    suspend fun insert(report: Report) {
        reportDao.insertReport(report)
    }

    suspend fun update(report: Report) {
        reportDao.updateReport(report)
    }

    suspend fun delete(report: Report) {
        reportDao.deleteReport(report)
    }
}