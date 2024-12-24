package com.canerture.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.canerture.feature.login.ui.R
import com.canerture.login.ui.component.buildDontHaveAnAccountSpannableText
import com.canerture.login.ui.component.buildPolicySpannableText
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppCheckBox
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppTextField
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
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
            text = stringResource(R.string.login_message),
            style = QuizAppTheme.typography.paragraph1,
        )
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppTextField(
            value = "",
            label = stringResource(R.string.login_email_username),
            icon = QuizAppTheme.icons.email,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppTextField(
            value = "",
            label = stringResource(R.string.login_email_username),
            icon = QuizAppTheme.icons.lock,
            isPassword = true,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QuizAppCheckBox(
                text = stringResource(R.string.remember_me),
                isChecked = false,
                onCheckedChange = {},
            )
            Spacer(modifier = Modifier.weight(1f))
            QuizAppText(
                text = stringResource(R.string.forgot_password),
                style = QuizAppTheme.typography.heading6,
                color = QuizAppTheme.colors.blue,
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        QuizAppButton(
            text = stringResource(R.string.login),
            onClick = {},
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppText(
            modifier = Modifier.noRippleClickable { onNavigateRegister() },
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
private fun LoginScreenPreview() {
    LoginScreen(
        onNavigateBack = {},
        onNavigateRegister = {},
        onNavigateHome = {},
    )
}