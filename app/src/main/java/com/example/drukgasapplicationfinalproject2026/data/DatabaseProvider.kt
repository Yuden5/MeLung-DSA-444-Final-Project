package com.example.drukgasapplicationfinalproject2026.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "drukgas_database"
            )
            .fallbackToDestructiveMigration()
            .build().also {
                INSTANCE = it
            }
        }
    }
}
