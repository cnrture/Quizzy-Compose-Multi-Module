package com.canerture.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    icon: ImageVector? = null,
) {
    val leadingIcon: @Composable (() -> Unit)? = icon?.let {
        {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }
    }

    var visibility by remember { mutableStateOf(false) }
    val visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation()

    val trailingIcon: @Composable (() -> Unit)? = if (isPassword) {
        {
            Icon(
                modifier = Modifier.clickable { visibility = !visibility },
                imageVector = if (visibility) QuizAppTheme.icons.visibilityOn else QuizAppTheme.icons.visibilityOff,
                contentDescription = null,
            )
        }
    } else null

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        value = value,
        onValueChange = onValueChange,
        label = {
            QuizAppText(
                text = label,
                style = QuizAppTheme.typography.paragraph1,
            )
        },
        textStyle = QuizAppTheme.typography.paragraph1,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = QuizAppTheme.colors.white,
            focusedContainerColor = QuizAppTheme.colors.white,
            focusedLabelColor = QuizAppTheme.colors.blue,
            focusedBorderColor = QuizAppTheme.colors.blue,
            unfocusedLabelColor = QuizAppTheme.colors.darkGray,
            unfocusedBorderColor = QuizAppTheme.colors.black,
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
    )
}

@Preview(showBackground = true)
@Composable
fun QuizAppTextFieldPreview() {
    Column {
        QuizAppTextField(
            value = "cnrture",
            label = "Email or Username",
            onValueChange = {},
            icon = QuizAppTheme.icons.email,
        )
        Spacer(modifier = Modifier.height(16.dp))
        QuizAppTextField(
            value = "123456",
            label = "Password",
            onValueChange = {},
            isPassword = true,
            icon = QuizAppTheme.icons.lock,
        )
    }
}