package com.canerture.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun QuizzyAsyncImage(
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

@PreviewLightDark
@Composable
fun QuizzyAsyncImagePreview() {
    QuizzyAsyncImage(
        modifier = Modifier.size(56.dp),
        imageUrl = "https://www.canerture.com/assets/images/canerture_logo.png",
        contentDescription = "Canerture Logo",
    )
}