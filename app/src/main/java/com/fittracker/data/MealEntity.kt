package com.fittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // BREAKFAST, LUNCH, DINNER, SNACK
    val calories: Int,
    val protein: Double, // in grams
    val carbs: Double, // in grams
    val fat: Double, // in grams
    val date: Long, // timestamp (start of day or full)
    val time: String // e.g., "08:30"
)
