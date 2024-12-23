package com.canerture.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

enum class QuizAppButtonType { PRIMARY, SECONDARY }

enum class QuizAppButtonSize { SMALL, MEDIUM, LARGE }

@Composable
fun QuizAppButton(
    modifier: Modifier = Modifier,
    text: String,
    type: QuizAppButtonType = QuizAppButtonType.PRIMARY,
    size: QuizAppButtonSize = QuizAppButtonSize.MEDIUM,
    onClick: () -> Unit,
) {
    val textStyle = when (size) {
        QuizAppButtonSize.SMALL -> QuizAppTheme.typography.heading6
        QuizAppButtonSize.MEDIUM -> QuizAppTheme.typography.heading5
        QuizAppButtonSize.LARGE -> QuizAppTheme.typography.heading4
    }
    when (type) {
        QuizAppButtonType.PRIMARY -> {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(QuizAppTheme.colors.blue),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = QuizAppTheme.colors.black),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            ) {
                Text(
                    text = text,
                    color = QuizAppTheme.colors.white,
                    style = textStyle,
                )
            }
        }

        QuizAppButtonType.SECONDARY -> {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(QuizAppTheme.colors.white),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = QuizAppTheme.colors.black),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            ) {
                Text(
                    text = text,
                    color = QuizAppTheme.colors.black,
                    style = textStyle,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ESButtonPreview() {
    QuizAppTheme {
        Column {
            QuizAppButton(
                text = "Primary Button",
                type = QuizAppButtonType.PRIMARY,
                size = QuizAppButtonSize.SMALL,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppButton(
                text = "Outlined Button",
                type = QuizAppButtonType.PRIMARY,
                size = QuizAppButtonSize.MEDIUM,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppButton(
                text = "Primary Button",
                type = QuizAppButtonType.PRIMARY,
                size = QuizAppButtonSize.LARGE,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppButton(
                text = "Primary Button",
                type = QuizAppButtonType.SECONDARY,
                size = QuizAppButtonSize.SMALL,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppButton(
                text = "Outlined Button",
                type = QuizAppButtonType.SECONDARY,
                size = QuizAppButtonSize.MEDIUM,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppButton(
                text = "Primary Button",
                type = QuizAppButtonType.SECONDARY,
                size = QuizAppButtonSize.LARGE,
                onClick = { }
            )
        }
    }
}