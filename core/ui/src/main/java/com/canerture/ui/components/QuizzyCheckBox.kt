package com.canerture.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizzyCheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    text: String? = null,
    testTag: String,
    style: TextStyle = QuizAppTheme.typography.paragraph2,
    onCheckedChange: (Boolean) -> Unit,
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isChecked) QuizAppTheme.colors.blue else QuizAppTheme.colors.background,
        label = "color",
    )

    Row(
        modifier = modifier.testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .boldBorder(4)
                .size(24.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
                .clickable {
                    onCheckedChange(!isChecked)
                }
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                AnimatedVisibility(
                    visible = isChecked,
                    enter = scaleIn(initialScale = 0.5f),
                    exit = shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(animatedColor)
                    )
                }
            }
        }
        text?.let {
            QuizzySpacer(8.dp)
            QuizzyText(
                testTag = "$testTag.label",
                text = it,
                style = style,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun QuizzyCheckBoxPreview() {
    Column {
        QuizzyCheckBox(
            testTag = "checkbox",
            isChecked = false,
            text = "Check me",
            onCheckedChange = {}
        )
        QuizzySpacer(16.dp)
        QuizzyCheckBox(
            testTag = "checkbox",
            isChecked = true,
            text = "Check me",
            onCheckedChange = {}
        )
    }
}