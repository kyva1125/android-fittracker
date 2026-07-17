package com.fittracker

import android.app.Application
import com.fittracker.data.AchievementEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FitTrackerApplication : Application() {

    @Inject
    lateinit var repository: FitTrackerRepository

    override fun onCreate() {
        super.onCreate()
        prepopulateAchievements()
    }

    private fun prepopulateAchievements() {
        CoroutineScope(Dispatchers.IO).launch {
            val achievements = listOf(
                AchievementEntity(
                    id = 1,
                    title = "First Step",
                    description = "Log your first active workout session",
                    iconRes = 0,
                    isUnlocked = false,
                    xpPoints = 100
                ),
                AchievementEntity(
                    id = 2,
                    title = "Hydration Hero",
                    description = "Reach your daily water intake goal",
                    iconRes = 0,
                    isUnlocked = false,
                    xpPoints = 100
                ),
                AchievementEntity(
                    id = 3,
                    title = "Macro Tracker",
                    description = "Log a meal with full macronutrients tracked",
                    iconRes = 0,
                    isUnlocked = false,
                    xpPoints = 150
                ),
                AchievementEntity(
                    id = 4,
                    title = "Stat Collector",
                    description = "Record your body weight or physical measurements",
                    iconRes = 0,
                    isUnlocked = false,
                    xpPoints = 200
                )
            )
            repository.insertAchievements(achievements)
        }
    }
}
