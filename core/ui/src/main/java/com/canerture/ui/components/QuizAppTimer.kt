package com.canerture.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

enum class TimerState {
    START,
    STOP,
    RESET,
}

@Composable
fun QuizAppTimer(
    modifier: Modifier = Modifier,
    state: TimerState,
    onTimeOut: () -> Unit,
) {
    val progressRatio = remember { Animatable(initialValue = 10f) }
    val circleStyle = Stroke(width = 30f)
    val arcStyle = Stroke(width = 30f, cap = StrokeCap.Round)
    val bgColor = QuizAppTheme.colors.lightBlue.copy(alpha = 0.5f)
    val trackColor = QuizAppTheme.colors.blue
    val lastColor = QuizAppTheme.colors.red

    var currentTime by remember { mutableIntStateOf(10) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (state) {
            TimerState.START -> {
                isRunning = false
                currentTime = 10
                progressRatio.snapTo(1f)
                isRunning = true
            }

            TimerState.STOP -> {
                isRunning = false
            }

            else -> {
                isRunning = true
                currentTime = 10
                progressRatio.snapTo(1f)
            }
        }
    }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (currentTime > 0) {
                val targetValue = (currentTime - 1).toFloat() / 10
                progressRatio.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
                )
                currentTime -= 1
            }
            isRunning = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val diameter = this.size.minDimension
            val radius = diameter / 2

            drawCircle(
                color = bgColor,
                radius = radius,
                style = circleStyle,
            )

            drawArc(
                color = if (currentTime <= 3) lastColor else trackColor,
                startAngle = -90f,
                sweepAngle = 360 * progressRatio.value,
                useCenter = false,
                style = arcStyle,
            )
        }
        if (currentTime == 0 && !isRunning) {
            QuizAppText(
                text = "Time's Up!",
                style = QuizAppTheme.typography.heading5,
            )
        } else {
            QuizAppText(
                text = currentTime.toString(),
                style = QuizAppTheme.typography.heading1,
            )
        }
        LaunchedEffect(currentTime) {
            if (currentTime == 0) {
                onTimeOut()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppTimerPreview() {
    QuizAppTimer(
        modifier = Modifier.size(200.dp),
        state = TimerState.START,
        onTimeOut = {},
    )
}