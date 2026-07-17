package com.fittracker.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fittracker.data.WorkoutSetEntity
import com.fittracker.ui.components.LoadingState

@Composable
fun ExerciseTrackerScreen(
    workoutId: Int,
    onBack: () -> Unit,
    onRestTimer: (Int) -> Unit,
    viewModel: ExerciseTrackerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.workout?.name ?: "Active Session") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingState()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.exercisesWithSets) { item ->
                        ExerciseTrackerCard(
                            exerciseWithSets = item,
                            onSetCheckChanged = { set ->
                                viewModel.toggleSetCompletion(set)
                                if (!set.isCompleted) { // Just completed the set
                                    onRestTimer(item.exercise.restTimeSeconds)
                                }
                            },
                            onSetUpdated = { set ->
                                viewModel.updateSet(set)
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.finishWorkout(onFinished = onBack)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Finish Workout & Log Stats", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ExerciseTrackerCard(
    exerciseWithSets: ExerciseWithSets,
    onSetCheckChanged: (WorkoutSetEntity) -> Unit,
    onSetUpdated: (WorkoutSetEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = exerciseWithSets.exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Target: ${exerciseWithSets.exercise.sets} sets × ${exerciseWithSets.exercise.reps} reps",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))

                // Set headers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Set", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text("Weight (kg)", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text("Reps", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))

                exerciseWithSets.sets.forEach { set ->
                    var repsVal by remember(set.id) { mutableStateOf(set.reps.toString()) }
                    var weightVal by remember(set.id) { mutableStateOf(set.weight.toString()) }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${set.setNumber}",
                            modifier = Modifier.width(40.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        OutlinedTextField(
                            value = weightVal,
                            onValueChange = {
                                weightVal = it
                                val d = it.toDoubleOrNull()
                                if (d != null) onSetUpdated(set.copy(weight = d))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            singleLine = true,
                            enabled = !set.isCompleted
                        )

                        OutlinedTextField(
                            value = repsVal,
                            onValueChange = {
                                repsVal = it
                                val r = it.toIntOrNull()
                                if (r != null) onSetUpdated(set.copy(reps = r))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            singleLine = true,
                            enabled = !set.isCompleted
                        )

                        FilledIconButton(
                            onClick = { onSetCheckChanged(set) },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (set.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Complete Set", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
