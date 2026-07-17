package com.fittracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MotivationalQuote(
    modifier: Modifier = Modifier
) {
    val quotes = remember {
        listOf(
            "The only bad workout is the one that didn't happen." to "Unknown",
            "Action is the foundational key to all success." to "Pablo Picasso",
            "Your body can stand almost anything. It's your mind that you have to convince." to "Unknown",
            "Success is what comes after your energy is spent." to "Unknown",
            "Energy and persistence conquer all things." to "Benjamin Franklin",
            "What hurts today makes you stronger tomorrow." to "Jay Cutler",
            "Believe you can and you're halfway there." to "Theodore Roosevelt"
        )
    }

    val randomQuote = remember { quotes.random() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "\"${randomQuote.first}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- ${randomQuote.second}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
