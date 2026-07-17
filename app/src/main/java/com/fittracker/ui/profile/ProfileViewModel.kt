package com.fittracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.UserEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: UserEntity? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        repository.getUser()
            .onEach { user ->
                _uiState.value = ProfileUiState(user = user, isLoading = false)
            }.launchIn(viewModelScope)
    }

    fun updateProfile(
        name: String,
        age: Int,
        weight: Double,
        height: Double,
        gender: String,
        goal: String,
        calorieGoal: Int,
        waterGoal: Int
    ) {
        viewModelScope.launch {
            val current = _uiState.value.user
            val updated = UserEntity(
                id = 1, // Single profile row
                name = name,
                age = age,
                weight = weight,
                height = height,
                gender = gender,
                fitnessGoal = goal,
                dailyCalorieGoal = calorieGoal,
                dailyWaterGoal = waterGoal,
                createdAt = current?.createdAt ?: System.currentTimeMillis()
            )
            repository.insertUser(updated)
        }
    }
}
