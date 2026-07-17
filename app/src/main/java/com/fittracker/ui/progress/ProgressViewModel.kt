package com.fittracker.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.AchievementEntity
import com.fittracker.data.BodyMeasurementEntity
import com.fittracker.data.WorkoutEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class ProgressUiState(
    val weightHistory: List<BodyMeasurementEntity> = emptyList(),
    val achievements: List<AchievementEntity> = emptyList(),
    val streakDays: Int = 0,
    val totalXp: Int = 0,
    val unlockedCount: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        observeProgressMetrics()
    }

    private fun observeProgressMetrics() {
        combine(
            repository.getAllMeasurements(),
            repository.getAllAchievements(),
            repository.getAllWorkouts(),
            repository.getTotalXpPoints(),
            repository.getUnlockedCount()
        ) { measurements, achievements, workouts, xp, unlockedCount ->
            val streak = calculateStreak(workouts)
            ProgressUiState(
                weightHistory = measurements.sortedBy { it.date },
                achievements = achievements,
                streakDays = streak,
                totalXp = xp ?: 0,
                unlockedCount = unlockedCount,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    private fun calculateStreak(workouts: List<WorkoutEntity>): Int {
        if (workouts.isEmpty()) return 0

        // Parse unique workout dates sorted descending (newest first)
        val workoutDates = workouts.map {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.date
            // Normalize to start of day
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }.distinct().sortedDescending()

        val todayCal = Calendar.getInstance()
        todayCal.set(Calendar.HOUR_OF_DAY, 0)
        todayCal.set(Calendar.MINUTE, 0)
        todayCal.set(Calendar.SECOND, 0)
        todayCal.set(Calendar.MILLISECOND, 0)
        val today = todayCal.timeInMillis

        // If no workout today or yesterday, streak is broken/0
        val latestWorkout = workoutDates.firstOrNull() ?: return 0
        val diffMs = today - latestWorkout
        val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)

        if (diffDays > 1) {
            return 0
        }

        var streak = 0
        var expectedDate = latestWorkout

        for (workoutDate in workoutDates) {
            if (workoutDate == expectedDate) {
                streak++
                expectedDate -= TimeUnit.DAYS.toMillis(1)
            } else if (workoutDate < expectedDate) {
                // Streak is broken
                break
            }
        }
        return streak
    }
}
