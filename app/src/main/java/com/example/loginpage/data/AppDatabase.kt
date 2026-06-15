package com.example.loginpage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loginpage.data.dao.PosyanduDao
import com.example.loginpage.data.entity.BalitaEntity
import com.example.loginpage.data.entity.CatatanEntity

@Database(entities = [BalitaEntity::class, CatatanEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun posyanduDao(): PosyanduDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "posyandu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}