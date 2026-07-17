package com.fittracker.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.MealEntity
import com.fittracker.network.NetworkResult
import com.fittracker.network.RecipeSearchResponse
import com.fittracker.network.models.RecipeApiResponse
import com.fittracker.repository.FitTrackerRepository
import com.fittracker.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val localRepository: FitTrackerRepository,
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _mealName = MutableStateFlow("")
    val mealName: StateFlow<String> = _mealName.asStateFlow()

    private val _mealType = MutableStateFlow("BREAKFAST")
    val mealType: StateFlow<String> = _mealType.asStateFlow()

    private val _calories = MutableStateFlow("")
    val calories: StateFlow<String> = _calories.asStateFlow()

    private val _protein = MutableStateFlow("")
    val protein: StateFlow<String> = _protein.asStateFlow()

    private val _carbs = MutableStateFlow("")
    val carbs: StateFlow<String> = _carbs.asStateFlow()

    private val _fat = MutableStateFlow("")
    val fat: StateFlow<String> = _fat.asStateFlow()

    // Remote recipe search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _apiResults = MutableStateFlow<NetworkResult<RecipeSearchResponse>?>(null)
    val apiResults: StateFlow<NetworkResult<RecipeSearchResponse>?> = _apiResults.asStateFlow()

    fun setMealName(name: String) { _mealName.value = name }
    fun setMealType(type: String) { _mealType.value = type }
    fun setCalories(cal: String) { _calories.value = cal }
    fun setProtein(prot: String) { _protein.value = prot }
    fun setCarbs(carb: String) { _carbs.value = carb }
    fun setFat(fat: String) { _fat.value = fat }
    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun searchRecipes() {
        val query = _searchQuery.value
        if (query.isBlank()) return

        viewModelScope.launch {
            healthRepository.searchRecipes(query).collect { result ->
                _apiResults.value = result
            }
        }
    }

    fun selectRecipe(recipe: RecipeApiResponse) {
        _mealName.value = recipe.label
        _calories.value = recipe.calories.toInt().toString()
        // Extract macros safely if present in the response
        val proteinG = recipe.nutrients?.protein?.quantity ?: 0.0
        val carbsG = recipe.nutrients?.carbs?.quantity ?: 0.0
        val fatG = recipe.nutrients?.fat?.quantity ?: 0.0

        _protein.value = proteinG.toInt().toString()
        _carbs.value = carbsG.toInt().toString()
        _fat.value = fatG.toInt().toString()
    }

    fun saveMeal(onSuccess: () -> Unit) {
        val name = _mealName.value
        val caloriesVal = _calories.value.toIntOrNull() ?: 0
        if (name.isBlank() || caloriesVal <= 0) return

        viewModelScope.launch {
            val meal = MealEntity(
                name = name,
                type = _mealType.value,
                calories = caloriesVal,
                protein = _protein.value.toDoubleOrNull() ?: 0.0,
                carbs = _carbs.value.toDoubleOrNull() ?: 0.0,
                fat = _fat.value.toDoubleOrNull() ?: 0.0,
                date = getTodayStartTimestamp(),
                time = getCurrentFormattedTime()
            )
            localRepository.insertMeal(meal)
            onSuccess()
        }
    }

    private fun getTodayStartTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getCurrentFormattedTime(): String {
        val formatter = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return formatter.format(java.util.Date())
    }
}
