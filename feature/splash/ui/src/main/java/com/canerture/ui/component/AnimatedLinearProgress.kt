package com.canerture.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.delay

@Composable
fun AnimatedLinearProgress() {
    var progressValue by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (progressValue < 100f) {
            progressValue = 0f
            repeat(100) {
                progressValue += 1f
                delay(20)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        LinearProgress(value = progressValue)
        QuizAppText(
            text = "${progressValue.toInt()}%",
            style = QuizAppTheme.typography.subheading2,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun LinearProgress(value: Float) {
    val yellow = QuizAppTheme.colors.yellow
    val softGray = QuizAppTheme.colors.softGray
    val progress = 1f / 100f * value
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(24.dp)
            .border(
                width = 2.dp,
                color = QuizAppTheme.colors.black,
                shape = CircleShape
            )
    ) {
        drawRoundRect(
            color = softGray,
            size = size.copy(width = size.width),
            cornerRadius = CornerRadius(100f)
        )

        drawRoundRect(
            color = yellow,
            size = size.copy(width = size.width * progress),
            cornerRadius = CornerRadius(100f)
        )
    }
}