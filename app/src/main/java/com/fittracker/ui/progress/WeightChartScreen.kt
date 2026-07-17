package com.fittracker.ui.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fittracker.data.BodyMeasurementEntity
import com.fittracker.ui.components.EmptyState
import com.fittracker.ui.components.LoadingState
import com.fittracker.utils.formatDate
import kotlinx.coroutines.launch

@Composable
fun WeightChartScreen(
    onBack: () -> Unit,
    viewModel: ProgressViewModel = hiltViewModel(),
    measurementViewModel: MeasurementViewModel = hiltViewModel() // Used for logging new weight
) {
    val uiState by viewModel.uiState.collectAsState()
    var newWeight by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weight History") },
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (uiState.weightHistory.isEmpty()) {
                    EmptyState(
                        message = "No weight logs yet. Log your first weight below!",
                        icon = Icons.Default.Scale,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    // Custom Canvas Line Chart Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Box(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                            WeightLineChart(weightLogs = uiState.weightHistory)
                        }
                    }
                }

                // Add Weight Log Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Log New Weight",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = newWeight,
                                onValueChange = { newWeight = it },
                                label = { Text("Weight (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    val w = newWeight.toDoubleOrNull()
                                    if (w != null) {
                                        scope.launch {
                                            measurementViewModel.logWeightOnly(w)
                                            newWeight = ""
                                        }
                                    }
                                },
                                enabled = newWeight.isNotBlank(),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text("Log")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeightLineChart(weightLogs: List<BodyMeasurementEntity>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        val width = size.width
        val height = size.height

        val minWeight = (weightLogs.minOfOrNull { it.weight } ?: 50.0) - 5.0
        val maxWeight = (weightLogs.maxOfOrNull { it.weight } ?: 100.0) + 5.0
        val weightRange = maxWeight - minWeight

        val pointsCount = weightLogs.size
        val xSpacing = if (pointsCount > 1) width / (pointsCount - 1) else width

        // Draw horizontal grid lines (Y axis helper lines)
        val gridLines = 4
        for (i in 0..gridLines) {
            val y = height - (i * (height / gridLines))
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        if (pointsCount > 0) {
            val path = Path()

            weightLogs.forEachIndexed { index, log ->
                val x = index * xSpacing
                // Map weight value to Y-axis (higher weight -> lower Y value in Canvas coords)
                val relativeWeight = (log.weight - minWeight) / weightRange
                val y = height - (relativeWeight.toFloat() * height)

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                // Draw dot points
                drawCircle(
                    color = primaryColor,
                    radius = 5.dp.toPx(),
                    center = Offset(x, y)
                )
            }

            // Draw line chart path
            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}
