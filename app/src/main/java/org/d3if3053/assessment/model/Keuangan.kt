package org.d3if3053.assessment.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keuangan")
data class Keuangan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val pilihan: String,
    val deskripsi: String,
    val jumlahUang: String,
    val tanggal: String
)
