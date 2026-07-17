package com.fittracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.UserEntity
import com.fittracker.data.WorkoutEntity
import com.fittracker.data.MealEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class HomeUiState(
    val user: UserEntity? = null,
    val caloriesConsumed: Int = 0,
    val caloriesBurned: Int = 0,
    val waterIntakeMl: Int = 0,
    val todayWorkouts: List<WorkoutEntity> = emptyList(),
    val todayMeals: List<MealEntity> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        ensureDefaultUserExists()
        observeTodayMetrics()
    }

    private fun ensureDefaultUserExists() {
        viewModelScope.launch {
            repository.getUser().first().let { user ->
                if (user == null) {
                    repository.insertUser(
                        UserEntity(
                            id = 1,
                            name = "Fitness Pioneer",
                            age = 28,
                            weight = 75.0,
                            height = 178.0,
                            gender = "Male",
                            fitnessGoal = "Muscle Gain",
                            dailyCalorieGoal = 2200,
                            dailyWaterGoal = 2500
                        )
                    )
                }
            }
        }
    }

    private fun observeTodayMetrics() {
        val (start, end) = getTodayStartAndEndTimestamps()

        combine(
            repository.getUser(),
            repository.getMealsByDateRange(start, end),
            repository.getWorkoutsByDateRange(start, end),
            repository.getWaterLogsByDateRange(start, end)
        ) { user, meals, workouts, waterLogs ->
            val totalCaloriesConsumed = meals.sumOf { it.calories }
            val totalCaloriesBurned = workouts.sumOf { it.caloriesBurned }
            val totalWater = waterLogs.sumOf { it.amountMl }

            HomeUiState(
                user = user,
                caloriesConsumed = totalCaloriesConsumed,
                caloriesBurned = totalCaloriesBurned,
                waterIntakeMl = totalWater,
                todayWorkouts = workouts,
                todayMeals = meals
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    private fun getTodayStartAndEndTimestamps(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.timeInMillis

        return Pair(start, end)
    }
}
