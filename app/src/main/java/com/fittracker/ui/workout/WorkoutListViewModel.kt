package com.fittracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.WorkoutEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedType = MutableStateFlow("ALL")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()

    val workouts: StateFlow<List<WorkoutEntity>> = combine(
        repository.getAllWorkouts(),
        _searchQuery,
        _selectedType
    ) { list, query, type ->
        list.filter { workout ->
            val matchesQuery = workout.name.contains(query, ignoreCase = true) ||
                    workout.notes.contains(query, ignoreCase = true)
            val matchesType = type == "ALL" || workout.type == type
            matchesQuery && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedType(type: String) {
        _selectedType.value = type
    }
}
