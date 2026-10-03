package com.canerture.quiz.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.feature.quiz.ui.R
import com.canerture.quiz.ui.QuizContract.QUESTION_DURATION_SECONDS
import com.canerture.quiz.ui.QuizTestTags
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun QuizzyTimer(
    remainingSeconds: Int,
    isRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    val progressRatio = remember { Animatable(initialValue = remainingSeconds.toProgressRatio()) }
    val circleStyle = Stroke(width = 30f)
    val arcStyle = Stroke(width = 30f, cap = StrokeCap.Round)
    val bgColor = QuizAppTheme.colors.lightBlue.copy(alpha = 0.5f)
    val trackColor = QuizAppTheme.colors.blue
    val lastColor = QuizAppTheme.colors.red

    // The countdown itself lives in QuizViewModel; this only animates the arc between ticks.
    LaunchedEffect(remainingSeconds, isRunning) {
        progressRatio.snapTo(remainingSeconds.toProgressRatio())
        if (isRunning && remainingSeconds > 0) {
            progressRatio.animateTo(
                targetValue = (remainingSeconds - 1).toProgressRatio(),
                animationSpec = tween(durationMillis = TICK_DURATION_MILLIS, easing = LinearEasing),
            )
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
                color = if (remainingSeconds <= LAST_SECONDS_THRESHOLD) lastColor else trackColor,
                startAngle = -90f,
                sweepAngle = 360 * progressRatio.value,
                useCenter = false,
                style = arcStyle,
            )
        }
        if (remainingSeconds == 0) {
            QuizzyText(
                testTag = QuizTestTags.TIMER_TIMES_UP_TEXT,
                text = stringResource(R.string.times_up),
                style = QuizAppTheme.typography.heading5,
            )
        } else {
            QuizzyText(
                testTag = QuizTestTags.TIMER_COUNTDOWN_TEXT,
                text = remainingSeconds.toString(),
                style = QuizAppTheme.typography.heading1,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun QuizzyTimerPreview() {
    QuizzyTimer(
        modifier = Modifier.size(200.dp),
        remainingSeconds = 7,
        isRunning = false,
    )
}

private fun Int.toProgressRatio(): Float = this / QUESTION_DURATION_SECONDS.toFloat()

private const val TICK_DURATION_MILLIS = 1_000
private const val LAST_SECONDS_THRESHOLD = 3