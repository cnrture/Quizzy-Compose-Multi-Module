package com.canerture.favorites.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.favorites.domain.model.FavoriteModel

class FavoritesPreviewProvider : PreviewParameterProvider<FavoritesContract.UiState> {
    override val values: Sequence<FavoritesContract.UiState>
        get() = sequenceOf(
            FavoritesContract.UiState(
                isLoading = false,
                favorites = listOf(
                    FavoriteModel(
                        id = 1,
                        name = "What is the capital of Turkey?",
                        category = "Geography",
                        questionCount = 4,
                        playedCount = 10,
                        imageUrl = "https://www.countryflags.io/TR/flat/64.png",
                    ),
                    FavoriteModel(
                        id = 2,
                        name = "What is the capital of France?",
                        category = "Geography",
                        questionCount = 4,
                        playedCount = 10,
                        imageUrl = "https://www.countryflags.io/FR/flat/64.png",
                    ),
                    FavoriteModel(
                        id = 3,
                        name = "What is the capital of Germany?",
                        category = "Geography",
                        questionCount = 4,
                        playedCount = 10,
                        imageUrl = "https://www.countryflags.io/DE/flat/64.png",
                    ),
                ),
            ),
            FavoritesContract.UiState(
                isLoading = false,
                favorites = emptyList(),
            ),
            FavoritesContract.UiState(
                isLoading = true,
                favorites = listOf(
                    FavoriteModel(
                        id = 1,
                        name = "What is the capital of Turkey?",
                        category = "Geography",
                        questionCount = 4,
                        playedCount = 10,
                        imageUrl = "https://www.countryflags.io/TR/flat/64.png",
                    ),
                    FavoriteModel(
                        id = 2,
                        name = "What is the capital of France?",
                        category = "Geography",
                        questionCount = 4,
                        playedCount = 10,
                        imageUrl = "https://www.countryflags.io/FR/flat/64.png",
                    ),
                    FavoriteModel(
                        id = 3,
                        name = "What is the capital of Germany?",
                        category = "Geography",
                        questionCount = 4,
                        playedCount = 10,
                        imageUrl = "https://www.countryflags.io/DE/flat/64.png",
                    ),
                ),
            ),
        )
}