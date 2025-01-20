package com.canerture.favorites.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FavoriteResponse(
    val id: Int? = null,
    val name: String? = null,
    val category: String? = null,
    val score: Int? = null,
    val questionCount: Int? = null,
    val favoriteCount: Int? = null,
    val playedCount: Int? = null,
    val description: String? = null,
    val imageUrl: String? = null,
)