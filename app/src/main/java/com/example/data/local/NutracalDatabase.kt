package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class, WeightEntity::class], version = 2, exportSchema = false)
abstract class NutracalDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun weightDao(): WeightDao

    companion object {
        @Volatile
        private var INSTANCE: NutracalDatabase? = null

        fun getDatabase(context: Context): NutracalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutracalDatabase::class.java,
                    "nutracal_database"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
