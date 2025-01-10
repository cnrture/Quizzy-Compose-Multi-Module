package com.canerture.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

enum class QuizAppButtonType { PRIMARY, SECONDARY }

enum class QuizAppButtonSize { EXTRA_SMALL, SMALL, MEDIUM, LARGE }

@Composable
fun QuizAppButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnable: Boolean = true,
    type: QuizAppButtonType = QuizAppButtonType.PRIMARY,
    size: QuizAppButtonSize = QuizAppButtonSize.MEDIUM,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    val textStyle = when (size) {
        QuizAppButtonSize.EXTRA_SMALL -> QuizAppTheme.typography.subheading3
        QuizAppButtonSize.SMALL -> QuizAppTheme.typography.heading6
        QuizAppButtonSize.MEDIUM -> QuizAppTheme.typography.heading5
        QuizAppButtonSize.LARGE -> QuizAppTheme.typography.heading4
    }

    val height = when (size) {
        QuizAppButtonSize.EXTRA_SMALL -> 34.dp
        QuizAppButtonSize.SMALL -> 53.dp
        QuizAppButtonSize.MEDIUM -> 56.dp
        QuizAppButtonSize.LARGE -> 59.dp
    }

    val paddingValues = when (size) {
        QuizAppButtonSize.EXTRA_SMALL -> PaddingValues(vertical = 8.dp, horizontal = 16.dp)
        else -> PaddingValues(vertical = 16.dp, horizontal = 24.dp)
    }

    when (type) {
        QuizAppButtonType.PRIMARY -> {
            Button(
                modifier = Modifier
                    .then(modifier)
                    .height(height),
                onClick = onClick,
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = QuizAppTheme.colors.blue,
                    disabledContainerColor = QuizAppTheme.colors.gray,
                ),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = QuizAppTheme.colors.black),
                contentPadding = paddingValues,
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        tint = Color.Unspecified,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                QuizAppText(
                    text = text,
                    color = QuizAppTheme.colors.white,
                    style = textStyle,
                )
            }
        }

        QuizAppButtonType.SECONDARY -> {
            Button(
                modifier = Modifier
                    .height(height)
                    .then(modifier),
                onClick = onClick,
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(QuizAppTheme.colors.white),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = QuizAppTheme.colors.black),
                contentPadding = paddingValues,
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        tint = Color.Unspecified,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                QuizAppText(
                    text = text,
                    style = textStyle,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizAppButtonPreview() {
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
                icon = QuizAppTheme.icons.google,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuizAppButton(
                text = "Primary Button",
                type = QuizAppButtonType.SECONDARY,
                size = QuizAppButtonSize.LARGE,
                icon = QuizAppTheme.icons.google,
                onClick = { }
            )
        }
    }
}