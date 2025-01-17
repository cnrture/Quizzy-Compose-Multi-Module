package com.canerture.favorites.domain.model

data class FavoriteModel(
    val id: Int,
    val name: String,
    val category: String,
    val questionCount: Int,
    val playedCount: Int,
    val imageUrl: String,
)
