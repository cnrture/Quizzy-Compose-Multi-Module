package com.canerture.detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.detail.ui.R
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun BoxScope.StartQuizButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .background(QuizAppTheme.colors.background),
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            thickness = 2.dp,
            color = QuizAppTheme.colors.onBackground,
        )
        QuizAppButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            text = stringResource(R.string.start_quiz),
            onClick = onClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartQuizButtonPreview() {
    Box {
        StartQuizButton(
            onClick = {},
        )
    }
}