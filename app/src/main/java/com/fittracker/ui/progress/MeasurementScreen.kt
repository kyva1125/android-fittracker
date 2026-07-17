package com.fittracker.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fittracker.ui.components.EmptyState
import com.fittracker.ui.components.LoadingState
import com.fittracker.utils.formatDate

@Composable
fun MeasurementScreen(
    onBack: () -> Unit,
    viewModel: MeasurementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var weight by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var muscle by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var chest by remember { mutableStateOf("") }
    var arm by remember { mutableStateOf("") }
    var thigh by remember { mutableStateOf("") }

    var isLoggingActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Body Measurements") },
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
                    .padding(16.dp)
            ) {
                if (isLoggingActive) {
                    // Logging form
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Log Metrics",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("Weight (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = fat,
                                onValueChange = { fat = it },
                                label = { Text("Body Fat %") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        OutlinedTextField(
                            value = muscle,
                            onValueChange = { muscle = it },
                            label = { Text("Muscle Mass (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = waist,
                                onValueChange = { waist = it },
                                label = { Text("Waist (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = chest,
                                onValueChange = { chest = it },
                                label = { Text("Chest (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = arm,
                                onValueChange = { arm = it },
                                label = { Text("Arm (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = thigh,
                                onValueChange = { thigh = it },
                                label = { Text("Thigh (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isLoggingActive = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }
                            Button(
                                onClick = {
                                    val w = weight.toDoubleOrNull() ?: 0.0
                                    val f = fat.toDoubleOrNull() ?: 0.0
                                    val m = muscle.toDoubleOrNull() ?: 0.0
                                    val wa = waist.toDoubleOrNull() ?: 0.0
                                    val ch = chest.toDoubleOrNull() ?: 0.0
                                    val ar = arm.toDoubleOrNull() ?: 0.0
                                    val th = thigh.toDoubleOrNull() ?: 0.0

                                    if (w > 0.0) {
                                        viewModel.logFullMeasurements(w, f, m, wa, ch, ar, th)
                                        isLoggingActive = false
                                        // Reset fields
                                        weight = ""; fat = ""; muscle = ""; waist = ""; chest = ""; arm = ""; thigh = ""
                                    }
                                },
                                enabled = weight.isNotBlank(),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save Logs")
                            }
                        }
                    }
                } else {
                    // History logs list
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "History Logs",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = { isLoggingActive = true }) {
                            Text("Add Logs")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (uiState.measurements.isEmpty()) {
                        EmptyState(
                            message = "No measurement logs found.",
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(uiState.measurements) { log ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = log.date.formatDate("EEE, MMM dd, yyyy"),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            IconButton(onClick = { viewModel.deleteMeasurement(log) }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text("Weight: ${log.weight} kg", style = MaterialTheme.typography.bodyMedium)
                                                Text("Body Fat: ${log.bodyFatPercentage}%", style = MaterialTheme.typography.bodyMedium)
                                                Text("Muscle: ${log.muscleMass} kg", style = MaterialTheme.typography.bodyMedium)
                                            }
                                            Column {
                                                Text("Waist: ${log.waist} cm", style = MaterialTheme.typography.bodyMedium)
                                                Text("Chest: ${log.chest} cm", style = MaterialTheme.typography.bodyMedium)
                                                Text("Arms/Thighs: ${log.arm}/${log.thigh} cm", style = MaterialTheme.typography.bodyMedium)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
