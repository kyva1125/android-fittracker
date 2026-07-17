package com.fittracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.ExerciseEntity
import com.fittracker.data.WorkoutEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TempExercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val restTimeSeconds: Int
)

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _workoutName = MutableStateFlow("")
    val workoutName: StateFlow<String> = _workoutName.asStateFlow()

    private val _workoutType = MutableStateFlow("STRENGTH")
    val workoutType: StateFlow<String> = _workoutType.asStateFlow()

    private val _intensityLevel = MutableStateFlow("Medium")
    val intensityLevel: StateFlow<String> = _intensityLevel.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _duration = MutableStateFlow(30)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _calories = MutableStateFlow(250)
    val calories: StateFlow<Int> = _calories.asStateFlow()

    private val _exercises = MutableStateFlow<List<TempExercise>>(emptyList())
    val exercises: StateFlow<List<TempExercise>> = _exercises.asStateFlow()

    fun setWorkoutName(name: String) { _workoutName.value = name }
    fun setWorkoutType(type: String) { _workoutType.value = type }
    fun setIntensityLevel(level: String) { _intensityLevel.value = level }
    fun setNotes(notes: String) { _notes.value = notes }
    fun setDuration(duration: Int) { _duration.value = duration }
    fun setCalories(calories: Int) { _calories.value = calories }

    fun addExercise(name: String, sets: Int, reps: Int, weight: Double, restTime: Int) {
        val newList = _exercises.value.toMutableList().apply {
            add(TempExercise(name, sets, reps, weight, restTime))
        }
        _exercises.value = newList
    }

    fun removeExercise(index: Int) {
        val newList = _exercises.value.toMutableList().apply {
            removeAt(index)
        }
        _exercises.value = newList
    }

    fun saveWorkout(onSuccess: () -> Unit) {
        if (_workoutName.value.isBlank()) return

        viewModelScope.launch {
            val workout = WorkoutEntity(
                name = _workoutName.value,
                type = _workoutType.value,
                durationMinutes = _duration.value,
                caloriesBurned = _calories.value,
                date = System.currentTimeMillis(),
                notes = _notes.value,
                intensityLevel = _intensityLevel.value
            )
            val workoutId = repository.insertWorkout(workout).toInt()

            _exercises.value.forEach { temp ->
                val exercise = ExerciseEntity(
                    workoutId = workoutId,
                    name = temp.name,
                    sets = temp.sets,
                    reps = temp.reps,
                    weight = temp.weight,
                    restTimeSeconds = temp.restTimeSeconds,
                    isCompleted = false
                )
                repository.insertExercise(exercise)
            }
            onSuccess()
        }
    }
}
