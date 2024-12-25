package com.canerture.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.canerture.core.ui.R
import com.canerture.ui.theme.QuizAppTheme

data class DialogState(
    val message: String? = null,
    val isSuccess: Boolean? = null,
)

@Composable
fun QuizAppDialog(
    message: String? = null,
    buttonText: String = stringResource(R.string.okay),
    isSuccess: Boolean? = null,
    isCancelable: Boolean = true,
    onDismiss: () -> Unit = {},
    onButtonClick: (() -> Unit)? = null,
) {
    val icon = when (isSuccess) {
        true -> Icons.Rounded.Check
        false -> Icons.Rounded.Clear
        else -> null
    }
    val iconBg = if (isSuccess == true) QuizAppTheme.colors.green else QuizAppTheme.colors.red
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = isCancelable,
            dismissOnClickOutside = isCancelable,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = QuizAppTheme.colors.white,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            icon?.let {
                Box(
                    modifier = Modifier
                        .background(
                            color = iconBg,
                            shape = RoundedCornerShape(50),
                        )
                        .padding(8.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = it,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
            message?.let {
                QuizAppText(
                    text = it,
                    style = QuizAppTheme.typography.subheading2,
                    color = QuizAppTheme.colors.black,
                    textAlign = TextAlign.Center,
                )
            }
            QuizAppButton(
                type = QuizAppButtonType.PRIMARY,
                size = QuizAppButtonSize.SMALL,
                text = buttonText,
                onClick = {
                    if (onButtonClick != null) {
                        onButtonClick()
                    } else {
                        onDismiss()
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppDialogPreview() {
    QuizAppDialog(
        message = "This is a sample error message",
        buttonText = "Okay",
    )
}