package com.fittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val iconRes: Int, // drawable resource ID
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null,
    val xpPoints: Int = 100
)
