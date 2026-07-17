package com.fittracker.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.BodyMeasurementEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MeasurementUiState(
    val measurements: List<BodyMeasurementEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class MeasurementViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeasurementUiState())
    val uiState: StateFlow<MeasurementUiState> = _uiState.asStateFlow()

    init {
        observeMeasurements()
    }

    private fun observeMeasurements() {
        repository.getAllMeasurements()
            .onEach { list ->
                _uiState.value = MeasurementUiState(measurements = list, isLoading = false)
            }.launchIn(viewModelScope)
    }

    fun logWeightOnly(weight: Double) {
        viewModelScope.launch {
            val entity = BodyMeasurementEntity(
                date = System.currentTimeMillis(),
                weight = weight,
                bodyFatPercentage = 0.0,
                muscleMass = 0.0,
                waist = 0.0,
                chest = 0.0,
                arm = 0.0,
                thigh = 0.0
            )
            repository.insertMeasurement(entity)
        }
    }

    fun logFullMeasurements(
        weight: Double,
        fat: Double,
        muscle: Double,
        waist: Double,
        chest: Double,
        arm: Double,
        thigh: Double
    ) {
        viewModelScope.launch {
            val entity = BodyMeasurementEntity(
                date = System.currentTimeMillis(),
                weight = weight,
                bodyFatPercentage = fat,
                muscleMass = muscle,
                waist = waist,
                chest = chest,
                arm = arm,
                thigh = thigh
            )
            repository.insertMeasurement(entity)
        }
    }

    fun deleteMeasurement(measurement: BodyMeasurementEntity) {
        viewModelScope.launch {
            repository.deleteMeasurement(measurement)
        }
    }
}
