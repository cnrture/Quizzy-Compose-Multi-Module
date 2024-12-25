package com.canerture.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppToolbar(
    title: String? = null,
    endIcon: ImageVector? = null,
    onBackClick: (() -> Unit)? = null,
    onEndIconClick: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp),
    ) {
        onBackClick?.let {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBackClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = QuizAppTheme.icons.arrowLeft,
                    contentDescription = null
                )
            }
        }
        title?.let {
            QuizAppText(
                modifier = Modifier.align(Alignment.CenterStart),
                text = title,
                style = QuizAppTheme.typography.heading2
            )
        }
        content?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                content()
            }
        }
        endIcon?.let {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onEndIconClick?.invoke() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = endIcon,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizAppToolbarPreview() {
    Column {
        QuizAppToolbar(
            onBackClick = { },
            endIcon = QuizAppTheme.icons.settings,
            onEndIconClick = { },
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppToolbar(
            title = "Title",
            endIcon = QuizAppTheme.icons.settings,
            onEndIconClick = { },
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppToolbar(
            title = "Title",
            content = {
                Row {
                    QuizAppText(text = "Content")
                }
            },
            endIcon = QuizAppTheme.icons.settings,
            onEndIconClick = { },
        )
    }
}