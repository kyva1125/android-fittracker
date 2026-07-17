package com.fittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1, // Single-user local profile
    val name: String,
    val age: Int,
    val weight: Double, // in kg
    val height: Double, // in cm
    val gender: String,
    val fitnessGoal: String, // e.g., "Weight Loss", "Muscle Gain", "Maintenance"
    val dailyCalorieGoal: Int,
    val dailyWaterGoal: Int, // in ml
    val createdAt: Long = System.currentTimeMillis()
)
