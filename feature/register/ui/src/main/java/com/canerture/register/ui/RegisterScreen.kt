package com.canerture.register.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.core.common.noRippleClickable
import com.canerture.feature.register.ui.R
import com.canerture.register.ui.component.buildDontHaveAnAccountSpannableText
import com.canerture.register.ui.component.buildPolicySpannableText
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppTextField
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Start),
            imageVector = QuizAppTheme.icons.arrowLeft,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppText(
            text = stringResource(R.string.welcome),
            style = QuizAppTheme.typography.heading1,
        )
        Spacer(modifier = Modifier.height(8.dp))
        QuizAppText(
            text = stringResource(R.string.register_message),
            style = QuizAppTheme.typography.paragraph1,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppTextField(
            value = "",
            label = stringResource(R.string.register_email),
            icon = QuizAppTheme.icons.email,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = "",
            label = stringResource(R.string.register_username),
            icon = QuizAppTheme.icons.profileUnselected,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = "",
            label = stringResource(R.string.password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = "",
            label = stringResource(R.string.register_confirm_password),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppButton(
            text = stringResource(R.string.register),
            onClick = {},
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            modifier = Modifier.noRippleClickable { onNavigateLogin() },
            text = buildDontHaveAnAccountSpannableText(),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppText(
            text = buildPolicySpannableText(),
            style = QuizAppTheme.typography.paragraph2,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        onNavigateBack = {},
        onNavigateLogin = {}
    )
}