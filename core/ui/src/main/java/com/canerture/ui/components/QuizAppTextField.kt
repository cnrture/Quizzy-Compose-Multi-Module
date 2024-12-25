package com.canerture.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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
    keyboardType: KeyboardType = KeyboardType.Text,
    isSingleLine: Boolean = true,
    isPassword: Boolean = false,
    icon: ImageVector? = null,
    onValueChange: (String) -> Unit,
) {
    val leadingIcon: @Composable (() -> Unit)? = icon?.let {
        {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp),
                imageVector = icon,
                tint = QuizAppTheme.colors.black,
                contentDescription = null,
            )
        }
    }

    var visibility by remember { mutableStateOf(false) }
    val visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation()

    val trailingIcon: @Composable (() -> Unit)? = if (isPassword) {
        {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .clickable { visibility = !visibility },
                imageVector = if (visibility) QuizAppTheme.icons.visibilityOn else QuizAppTheme.icons.visibilityOff,
                tint = QuizAppTheme.colors.black,
                contentDescription = null,
            )
        }
    } else {
        null
    }

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
        singleLine = isSingleLine,
        maxLines = if (isSingleLine) 1 else Int.MAX_VALUE,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
        visualTransformation = if (isPassword) visualTransformation else VisualTransformation.None,
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizAppTextFieldPreview() {
    Column {
        QuizAppTextField(
            value = "",
            label = "Email or Username",
            onValueChange = {},
            icon = QuizAppTheme.icons.email,
        )
        Spacer(modifier = Modifier.height(16.dp))
        QuizAppTextField(
            value = "",
            label = "Password",
            onValueChange = {},
            isPassword = true,
            icon = QuizAppTheme.icons.lock,
        )
    }
}