package com.canerture.favorites.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.favorites.ui.FavoritesTestTags
import com.canerture.feature.favorites.ui.R
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun EmptyScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier.size(144.dp),
            imageVector = QuizAppTheme.icons.sad,
            tint = QuizAppTheme.colors.red,
            contentDescription = stringResource(R.string.empty_content_icon),
        )
        QuizzySpacer(48.dp)
        QuizzyText(
            testTag = FavoritesTestTags.EMPTY_TEXT,
            text = stringResource(R.string.empty_content),
            style = QuizAppTheme.typography.heading2,
            color = QuizAppTheme.colors.red,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewLightDark
@Composable
internal fun EmptyScreenContentPreview() {
    EmptyScreenContent()
}