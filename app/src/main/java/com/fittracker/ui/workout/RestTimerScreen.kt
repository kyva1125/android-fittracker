package com.fittracker.ui.workout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fittracker.utils.WorkoutTimer
import kotlinx.coroutines.delay

@Composable
fun RestTimerScreen(
    durationSeconds: Int,
    onTimerFinished: () -> Unit,
    onSkip: () -> Unit
) {
    val workoutTimer = remember { WorkoutTimer() }
    val timeLeft by workoutTimer.timeLeft.collectAsState()
    val isRunning by workoutTimer.isRunning.collectAsState()

    var initialDuration by remember { mutableStateOf(durationSeconds) }

    LaunchedEffect(key1 = initialDuration) {
        workoutTimer.startTimer(initialDuration, onFinish = onTimerFinished)
    }

    DisposableEffect(Unit) {
        onDispose {
            workoutTimer.cancel()
        }
    }

    val progress = if (initialDuration > 0) timeLeft.toFloat() / initialDuration.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "rest_timer_progress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Rest Period",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Take a breath and recover",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(240.dp)
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 16.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${timeLeft}s",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    initialDuration += 30
                    workoutTimer.startTimer(timeLeft + 30, onFinish = onTimerFinished)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text("+30s")
            }

            Button(
                onClick = {
                    if (isRunning) {
                        workoutTimer.pauseTimer()
                    } else {
                        workoutTimer.resumeTimer(onFinish = onTimerFinished)
                    }
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (isRunning) "Pause" else "Resume")
            }

            TextButton(
                onClick = onSkip,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Skip Rest")
            }
        }
    }
}
