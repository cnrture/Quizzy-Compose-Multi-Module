package com.canerture.home.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesResponse(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val quizCount: Int? = null,
)
