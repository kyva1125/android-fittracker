package com.fittracker.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutTimer {
    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null
    private val timerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun startTimer(durationSeconds: Int, onFinish: (() -> Unit)? = null) {
        timerJob?.cancel()
        _timeLeft.value = durationSeconds
        _isRunning.value = true

        timerJob = timerScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            _isRunning.value = false
            withContext(Dispatchers.Main) {
                onFinish?.invoke()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _isRunning.value = false
    }

    fun resumeTimer(onFinish: (() -> Unit)? = null) {
        if (_timeLeft.value > 0 && !_isRunning.value) {
            startTimer(_timeLeft.value, onFinish)
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timeLeft.value = 0
        _isRunning.value = false
    }

    fun cancel() {
        timerJob?.cancel()
        timerScope.cancel()
    }
}
