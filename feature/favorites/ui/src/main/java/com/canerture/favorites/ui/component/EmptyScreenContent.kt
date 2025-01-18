package com.canerture.favorites.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.favorites.ui.R
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun EmptyScreenContent() {
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
            contentDescription = "Empty Screen",
        )
        Spacer(modifier = Modifier.height(48.dp))
        QuizAppText(
            text = stringResource(R.string.empty_content),
            style = QuizAppTheme.typography.heading2,
            color = QuizAppTheme.colors.red,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyScreenContentPreview() {
    EmptyScreenContent()
}