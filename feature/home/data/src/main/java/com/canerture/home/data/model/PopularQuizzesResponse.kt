package com.canerture.home.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PopularQuizzesResponse(
    val id: Int? = null,
    val category: CategoriesResponse? = null,
    val name: String? = null,
    val score: Int? = null,
    val questionCount: Int? = null,
    val favoriteCount: Int? = null,
    val description: String? = null,
    val imageUrl: String? = null
)