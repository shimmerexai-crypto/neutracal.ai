package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mealTitle: String,
    val mealType: String,
    val timestamp: Long,
    val calories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val glycemicLevel: String,
    val cookingStyle: String,
    val itemsSummary: String,
    val spikeExplanation: String,
    val desiHealthFix: String,
    val thumbnailBase64: String? = null
)
