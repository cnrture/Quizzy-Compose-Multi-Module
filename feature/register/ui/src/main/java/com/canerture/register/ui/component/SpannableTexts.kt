package com.canerture.register.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.canerture.feature.register.ui.R
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun buildDontHaveAnAccountSpannableText() = buildAnnotatedString {
    withStyle(style = QuizAppTheme.typography.paragraph2.toSpanStyle()) {
        val normalText = stringResource(R.string.already_have_an_account)
        val spanText = stringResource(R.string.already_have_an_account_span)
        val mStartIndex = normalText.indexOf(spanText)
        val mEndIndex = mStartIndex.plus(spanText.length)

        append(normalText)
        addStyle(
            style = SpanStyle(
                color = QuizAppTheme.colors.blue,
                fontWeight = FontWeight.Bold,
            ),
            start = mStartIndex,
            end = mEndIndex,
        )
    }
}

@Composable
fun buildPolicySpannableText() = buildAnnotatedString {
    withStyle(style = QuizAppTheme.typography.paragraph2.toSpanStyle()) {
        val normalText = stringResource(R.string.policy)
        val spanTexts = listOf(
            stringResource(R.string.privacy_policy_span),
            stringResource(R.string.terms_of_conditions_span)
        )

        append(normalText)
        spanTexts.forEach {
            val mStartIndex = normalText.indexOf(it)
            val mEndIndex = mStartIndex.plus(it.length)
            addStyle(
                style = SpanStyle(
                    color = QuizAppTheme.colors.blue,
                    fontWeight = FontWeight.Bold,
                ),
                start = mStartIndex,
                end = mEndIndex,
            )
        }
    }
}