package com.fittracker.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.ExerciseEntity
import com.fittracker.data.WorkoutEntity
import com.fittracker.data.WorkoutSetEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseWithSets(
    val exercise: ExerciseEntity,
    val sets: List<WorkoutSetEntity>
)

data class ExerciseTrackerUiState(
    val workout: WorkoutEntity? = null,
    val exercisesWithSets: List<ExerciseWithSets> = emptyList(),
    val isLoading: Boolean = true,
    val activeRestTimeSeconds: Int = 60
)

@HiltViewModel
class ExerciseTrackerViewModel @Inject constructor(
    private val repository: FitTrackerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val workoutId: Int = savedStateHandle.get<Int>("workoutId") ?: 0

    private val _uiState = MutableStateFlow(ExerciseTrackerUiState())
    val uiState: StateFlow<ExerciseTrackerUiState> = _uiState.asStateFlow()

    init {
        loadSessionData()
    }

    private fun loadSessionData() {
        if (workoutId <= 0) {
            _uiState.value = ExerciseTrackerUiState(isLoading = false)
            return
        }

        viewModelScope.launch {
            // Wait for workout to load
            val workout = repository.getWorkoutById(workoutId).filterNotNull().first()
            
            // Collect exercises
            repository.getExercisesForWorkout(workoutId).collect { exercises ->
                val list = mutableListOf<ExerciseWithSets>()
                for (exercise in exercises) {
                    // Check if sets already exist in DB
                    var sets = repository.getSetsForExercise(exercise.id).first()
                    if (sets.isEmpty()) {
                        // Pre-populate sets in DB based on exercise target sets count
                        for (i in 1..exercise.sets) {
                            repository.insertSet(
                                WorkoutSetEntity(
                                    exerciseId = exercise.id,
                                    setNumber = i,
                                    reps = exercise.reps,
                                    weight = exercise.weight,
                                    isCompleted = false
                                )
                            )
                        }
                        sets = repository.getSetsForExercise(exercise.id).first()
                    }
                    list.add(ExerciseWithSets(exercise, sets))
                }

                _uiState.value = ExerciseTrackerUiState(
                    workout = workout,
                    exercisesWithSets = list,
                    isLoading = false,
                    activeRestTimeSeconds = exercises.firstOrNull()?.restTimeSeconds ?: 60
                )
            }
        }
    }

    fun updateSet(set: WorkoutSetEntity) {
        viewModelScope.launch {
            repository.updateSet(set)
            refreshSets(set.exerciseId)
        }
    }

    fun toggleSetCompletion(set: WorkoutSetEntity) {
        viewModelScope.launch {
            repository.updateSet(set.copy(isCompleted = !set.isCompleted))
            refreshSets(set.exerciseId)
        }
    }

    private suspend fun refreshSets(exerciseId: Int) {
        // Refreshes ui state list
        val currentList = _uiState.value.exercisesWithSets.toMutableList()
        val index = currentList.indexOfFirst { it.exercise.id == exerciseId }
        if (index != -1) {
            val updatedSets = repository.getSetsForExercise(exerciseId).first()
            currentList[index] = currentList[index].copy(sets = updatedSets)
            _uiState.value = _uiState.value.copy(exercisesWithSets = currentList)
        }
    }

    fun finishWorkout(onFinished: () -> Unit) {
        viewModelScope.launch {
            // Mark all exercises as completed
            _uiState.value.exercisesWithSets.forEach { item ->
                repository.updateExercise(item.exercise.copy(isCompleted = true))
            }
            // Trigger standard workout achievements unlock checks!
            repository.unlockAchievement(1) // E.g., unlock "First Workout Done!"
            onFinished()
        }
    }
}
