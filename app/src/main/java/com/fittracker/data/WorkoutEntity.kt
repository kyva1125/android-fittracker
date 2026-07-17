package com.fittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // CARDIO, STRENGTH, FLEXIBILITY, SPORTS
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val date: Long,
    val notes: String,
    val intensityLevel: String // Low, Medium, High
)
