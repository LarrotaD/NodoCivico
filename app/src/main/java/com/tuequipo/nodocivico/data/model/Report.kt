package com.tuequipo.nodocivico.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,       // ALTA / MEDIA / BAJA
    val location: String,
    val status: String = "PENDIENTE",
    val createdAt: String,
    val imageUri: String? = null,
    // NUEVO CAMPO OBLIGATORIO PARA EL ENTREGABLE 3:
    val isSynced: Boolean = false
)