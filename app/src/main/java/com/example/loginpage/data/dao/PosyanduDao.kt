package com.example.loginpage.data.dao

import androidx.room.*
import com.example.loginpage.data.entity.BalitaEntity
import com.example.loginpage.data.entity.CatatanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PosyanduDao {
    // Balita
    @Insert
    fun insertBalita(balita: BalitaEntity)

    @Update
    fun updateBalita(balita: BalitaEntity)

    @Delete
    fun deleteBalita(balita: BalitaEntity)

    @Query("SELECT * FROM balita ORDER BY id DESC")
    fun getAllBalita(): Flow<List<BalitaEntity>>

    // Catatan
    @Insert
    fun insertCatatan(catatan: CatatanEntity)

    @Update
    fun updateCatatan(catatan: CatatanEntity)

    @Delete
    fun deleteCatatan(catatan: CatatanEntity)

    @Query("SELECT * FROM catatan ORDER BY id DESC")
    fun getAllCatatan(): Flow<List<CatatanEntity>>
}