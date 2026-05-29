package com.tuequipo.nodocivico.data.remote

import com.tuequipo.nodocivico.data.model.Report
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // CONSUMO REAL: Obtener los reportes globales almacenados en el servidor remoto
    @GET("api/reports")
    suspend fun getRemoteReports(): List<Report>

    // CONSUMO REAL: Enviar un nuevo reporte generado de manera offline hacia el servidor
    @POST("api/reports")
    suspend fun uploadReport(@Body report: Report): Report
}