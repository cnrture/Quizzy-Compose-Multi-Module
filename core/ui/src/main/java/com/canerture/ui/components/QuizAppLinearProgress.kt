package com.canerture.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.conditional
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppLinearProgress(
    modifier: Modifier = Modifier,
    value: Int,
    maxValue: Int = 100,
    thickness: Dp = 24.dp,
    isBordered: Boolean = true,
    progressColor: Color = QuizAppTheme.colors.yellow,
    backgroundColor: Color = QuizAppTheme.colors.onBackground.copy(alpha = 0.5f),
) {
    val progress = 1f / maxValue * value
    Canvas(
        modifier = modifier
            .height(thickness)
            .conditional(isBordered) {
                boldBorder(100)
            }
    ) {
        drawRoundRect(
            color = backgroundColor,
            size = size.copy(width = size.width),
            cornerRadius = CornerRadius(100f)
        )

        drawRoundRect(
            color = progressColor,
            size = size.copy(width = size.width * progress),
            cornerRadius = CornerRadius(100f)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    QuizAppTheme {
        Surface(modifier = Modifier.background(QuizAppTheme.colors.background)) {
            QuizAppLinearProgress(value = 50, modifier = Modifier.fillMaxWidth())
        }
    }
}