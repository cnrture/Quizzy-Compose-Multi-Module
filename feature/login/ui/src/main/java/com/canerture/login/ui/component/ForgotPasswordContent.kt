package com.canerture.login.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.feature.login.ui.R
import com.canerture.ui.components.QuizzyButton
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.components.QuizzyTextField
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
        QuizzyText(
            text = stringResource(R.string.forgot_password_title),
            style = QuizAppTheme.typography.heading2,
        )
        QuizzySpacer(12.dp)
        QuizzyText(
            text = stringResource(R.string.forgot_password_message),
            style = QuizAppTheme.typography.subheading1,
        )
        QuizzySpacer(12.dp)
        QuizzyTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = stringResource(R.string.forgot_password_email),
        )
        QuizzySpacer(32.dp)
        QuizzyButton(
            text = stringResource(R.string.send_reset_link),
            onClick = { onSendResetLinkClick() },
        )
    }
}

@PreviewLightDark
@Composable
internal fun ForgotPasswordContentPreview() {
    ForgotPasswordContent(
        email = "",
        onEmailChange = {},
        onSendResetLinkClick = {}
    )
}