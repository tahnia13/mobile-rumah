package com.example.loginpage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catatan")
data class CatatanEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val judul: String,
    val isi: String,
    val tanggal: String
)