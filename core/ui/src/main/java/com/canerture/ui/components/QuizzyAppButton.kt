package com.canerture.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

enum class QuizzyButtonType { PRIMARY, SECONDARY }

enum class QuizzyButtonSize { EXTRA_SMALL, SMALL, MEDIUM, LARGE }

@Composable
fun QuizzyButton(
    modifier: Modifier = Modifier,
    text: String,
    testTag: String,
    isEnable: Boolean = true,
    type: QuizzyButtonType = QuizzyButtonType.PRIMARY,
    size: QuizzyButtonSize = QuizzyButtonSize.MEDIUM,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    val textStyle = when (size) {
        QuizzyButtonSize.EXTRA_SMALL -> QuizAppTheme.typography.subheading3
        QuizzyButtonSize.SMALL -> QuizAppTheme.typography.heading6
        QuizzyButtonSize.MEDIUM -> QuizAppTheme.typography.heading5
        QuizzyButtonSize.LARGE -> QuizAppTheme.typography.heading4
    }

    val height = when (size) {
        QuizzyButtonSize.EXTRA_SMALL -> 34.dp
        QuizzyButtonSize.SMALL -> 53.dp
        QuizzyButtonSize.MEDIUM -> 56.dp
        QuizzyButtonSize.LARGE -> 59.dp
    }

    val paddingValues = when (size) {
        QuizzyButtonSize.EXTRA_SMALL -> PaddingValues(vertical = 8.dp, horizontal = 16.dp)
        else -> PaddingValues(vertical = 16.dp, horizontal = 24.dp)
    }

    when (type) {
        QuizzyButtonType.PRIMARY -> {
            Button(
                modifier = Modifier
                    .then(modifier)
                    .height(height)
                    .testTag(testTag),
                onClick = onClick,
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = QuizAppTheme.colors.blue,
                    disabledContainerColor = QuizAppTheme.colors.onBackground.copy(alpha = 0.2f),
                ),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = QuizAppTheme.colors.onBackground),
                contentPadding = paddingValues,
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        tint = Color.Unspecified,
                        contentDescription = text,
                    )
                    QuizzySpacer(8.dp)
                }
                QuizzyText(
                    testTag = "$testTag.text",
                    text = text,
                    color = QuizAppTheme.colors.background,
                    style = textStyle,
                )
            }
        }

        QuizzyButtonType.SECONDARY -> {
            Button(
                modifier = Modifier
                    .height(height)
                    .then(modifier)
                    .testTag(testTag),
                onClick = onClick,
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(QuizAppTheme.colors.background),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = QuizAppTheme.colors.onBackground),
                contentPadding = paddingValues,
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        tint = Color.Unspecified,
                        contentDescription = text,
                    )
                    QuizzySpacer(8.dp)
                }
                QuizzyText(
                    testTag = "$testTag.text",
                    text = text,
                    style = textStyle,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun QuizzyButtonPreview() {
    QuizAppTheme {
        Column {
            QuizzyButton(
                text = "Primary Button",
                testTag = "primary_button",
                type = QuizzyButtonType.PRIMARY,
                size = QuizzyButtonSize.SMALL,
                onClick = { }
            )
            QuizzySpacer(16.dp)
            QuizzyButton(
                text = "Outlined Button",
                testTag = "outlined_button",
                type = QuizzyButtonType.PRIMARY,
                size = QuizzyButtonSize.MEDIUM,
                onClick = { }
            )
            QuizzySpacer(16.dp)
            QuizzyButton(
                text = "Primary Button",
                testTag = "primary_button",
                type = QuizzyButtonType.PRIMARY,
                size = QuizzyButtonSize.LARGE,
                onClick = { }
            )
            QuizzySpacer(16.dp)
            QuizzyButton(
                text = "Primary Button",
                testTag = "primary_button",
                type = QuizzyButtonType.SECONDARY,
                size = QuizzyButtonSize.SMALL,
                onClick = { }
            )
            QuizzySpacer(16.dp)
            QuizzyButton(
                text = "Outlined Button",
                testTag = "outlined_button",
                type = QuizzyButtonType.SECONDARY,
                size = QuizzyButtonSize.MEDIUM,
                icon = QuizAppTheme.icons.google,
                onClick = { }
            )
            QuizzySpacer(16.dp)
            QuizzyButton(
                text = "Primary Button",
                testTag = "primary_button",
                type = QuizzyButtonType.SECONDARY,
                size = QuizzyButtonSize.LARGE,
                icon = QuizAppTheme.icons.google,
                onClick = { }
            )
        }
    }
}