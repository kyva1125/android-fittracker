package com.fittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_measurements")
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long, // timestamp
    val weight: Double, // in kg
    val bodyFatPercentage: Double, // %
    val muscleMass: Double, // in kg
    val waist: Double, // in cm
    val chest: Double, // in cm
    val arm: Double, // in cm
    val thigh: Double // in cm
)
