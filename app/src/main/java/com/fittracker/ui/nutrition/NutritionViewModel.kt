package com.fittracker.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.MealEntity
import com.fittracker.data.UserEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class NutritionUiState(
    val user: UserEntity? = null,
    val meals: List<MealEntity> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Double = 0.0,
    val totalCarbs: Double = 0.0,
    val totalFat: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    init {
        observeNutritionData()
    }

    private fun observeNutritionData() {
        val (start, end) = getTodayStartAndEndTimestamps()

        combine(
            repository.getUser(),
            repository.getMealsByDateRange(start, end)
        ) { user, meals ->
            val totalCal = meals.sumOf { it.calories }
            val totalProt = meals.sumOf { it.protein }
            val totalCarb = meals.sumOf { it.carbs }
            val totalFt = meals.sumOf { it.fat }

            NutritionUiState(
                user = user,
                meals = meals,
                totalCalories = totalCal,
                totalProtein = totalProt,
                totalCarbs = totalCarb,
                totalFat = totalFt,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
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
