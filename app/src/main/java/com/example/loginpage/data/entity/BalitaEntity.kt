package com.example.loginpage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balita")
data class BalitaEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val nama: String,
    val usia: String,
    val berat: String,
    val tinggi: String
)