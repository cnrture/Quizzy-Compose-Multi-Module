package com.canerture.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun QuizAppAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String,
) {
    AsyncImage(
        modifier = modifier,
        model = imageUrl,
        contentScale = contentScale,
        contentDescription = contentDescription,
    )
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