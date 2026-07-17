package com.fittracker.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittracker.data.UserEntity
import com.fittracker.data.WaterIntakeEntity
import com.fittracker.repository.FitTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class WaterUiState(
    val user: UserEntity? = null,
    val waterIntakeLogs: List<WaterIntakeEntity> = emptyList(),
    val totalAmountMl: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val repository: FitTrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WaterUiState())
    val uiState: StateFlow<WaterUiState> = _uiState.asStateFlow()

    init {
        observeWaterData()
    }

    private fun observeWaterData() {
        val (start, end) = getTodayStartAndEndTimestamps()

        combine(
            repository.getUser(),
            repository.getWaterLogsByDateRange(start, end)
        ) { user, logs ->
            val total = logs.sumOf { it.amountMl }
            WaterUiState(
                user = user,
                waterIntakeLogs = logs,
                totalAmountMl = total,
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            val log = WaterIntakeEntity(
                date = getTodayStartTimestamp(),
                amountMl = amountMl
            )
            repository.insertWaterLog(log)
        }
    }

    fun clearTodayLogs() {
        viewModelScope.launch {
            repository.clearWaterLogsForDate(getTodayStartTimestamp())
        }
    }

    private fun getTodayStartTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
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
