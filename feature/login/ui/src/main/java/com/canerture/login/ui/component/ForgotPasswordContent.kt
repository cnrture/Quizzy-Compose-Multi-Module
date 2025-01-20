package com.canerture.login.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.login.ui.R
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppTextField
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun ForgotPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendResetLinkClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
    ) {
        QuizAppText(
            text = stringResource(R.string.forgot_password_title),
            style = QuizAppTheme.typography.heading2,
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppText(
            text = stringResource(R.string.forgot_password_message),
            style = QuizAppTheme.typography.subheading1,
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = stringResource(R.string.forgot_password_email),
        )
        Spacer(modifier = Modifier.height(32.dp))
        QuizAppButton(
            text = stringResource(R.string.send_reset_link),
            onClick = { onSendResetLinkClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun ForgotPasswordContentPreview() {
    ForgotPasswordContent(
        email = "",
        onEmailChange = {},
        onSendResetLinkClick = {}
    )
}