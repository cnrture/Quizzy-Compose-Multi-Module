package com.canerture.home.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class CategoriesResponse(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val quizCount: Int? = null,
)
