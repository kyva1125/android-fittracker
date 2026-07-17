package com.fittracker.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.ExerciseEntity
import com.fittracker.data.WorkoutEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutDetailUiState(
    val workout: WorkoutEntity? = null,
    val exercises: List<ExerciseEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val repository: FitTrackerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val workoutId: Int = savedStateHandle.get<Int>("workoutId") ?: 0

    private val _uiState = MutableStateFlow(WorkoutDetailUiState())
    val uiState: StateFlow<WorkoutDetailUiState> = _uiState.asStateFlow()

    init {
        loadWorkoutDetails()
    }

    private fun loadWorkoutDetails() {
        if (workoutId <= 0) {
            _uiState.value = WorkoutDetailUiState(isLoading = false)
            return
        }

        combine(
            repository.getWorkoutById(workoutId),
            repository.getExercisesForWorkout(workoutId)
        ) { workout, exercises ->
            WorkoutDetailUiState(
                workout = workout,
                exercises = exercises,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun toggleExerciseCompletion(exercise: ExerciseEntity) {
        viewModelScope.launch {
            repository.updateExercise(exercise.copy(isCompleted = !exercise.isCompleted))
        }
    }

    fun deleteWorkout() {
        viewModelScope.launch {
            _uiState.value.workout?.let {
                repository.deleteWorkout(it)
            }
        }
    }
}
