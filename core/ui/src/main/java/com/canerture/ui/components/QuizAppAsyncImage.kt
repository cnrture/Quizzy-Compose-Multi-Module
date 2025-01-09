package com.canerture.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = imageUrl,
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = {
                isLoading = false
                isError = true
            },
            contentDescription = contentDescription,
        )

        if (isLoading) CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            color = QuizAppTheme.colors.blue,
            strokeCap = StrokeCap.Round,
        )

        if (isError) {
            Icon(
                imageVector = QuizAppTheme.icons.profileSelected,
                contentDescription = "Error",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppAsyncImagePreview() {
    QuizAppAsyncImage(
        modifier = Modifier.size(56.dp),
        imageUrl = "https://www.canerture.com/assets/images/canerture_logo.png",
        contentDescription = "Canerture Logo",
    )
}