package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: WeightEntity): Long

    @Delete
    suspend fun deleteWeight(weight: WeightEntity)

    @Query("SELECT * FROM weight_logs ORDER BY timestamp ASC")
    fun getAllWeightLogs(): Flow<List<WeightEntity>>

    @Query("SELECT * FROM weight_logs ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestWeight(): WeightEntity?
}
