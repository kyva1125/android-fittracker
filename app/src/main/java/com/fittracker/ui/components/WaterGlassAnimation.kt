package com.fittracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WaterGlassAnimation(
    currentMl: Int,
    goalMl: Int,
    modifier: Modifier = Modifier
) {
    val fillPercentage = if (goalMl > 0) currentMl.toFloat() / goalMl.toFloat() else 0f
    val animatedFill by animateFloatAsState(
        targetValue = fillPercentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "water_fill"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(width = 120.dp, height = 180.dp)
        ) {
            val waterColor = Color(0xFF3B82F6) // Electric Blue
            val glassOutlineColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)

            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Draw glass background/container
                drawRoundRect(
                    color = glassOutlineColor,
                    topLeft = Offset(0f, 0f),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(16f, 16f),
                    style = Stroke(width = 4.dp.toPx())
                )

                // Calculate water level height
                val waterHeight = height * animatedFill
                val waterTop = height - waterHeight

                // Draw filled water
                if (waterHeight > 0f) {
                    drawRoundRect(
                        color = waterColor,
                        topLeft = Offset(4.dp.toPx(), waterTop + 4.dp.toPx()),
                        size = Size(width - 8.dp.toPx(), waterHeight - 8.dp.toPx()),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(fillPercentage * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (fillPercentage > 0.4f) Color.White else MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$currentMl / $goalMl ml",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
