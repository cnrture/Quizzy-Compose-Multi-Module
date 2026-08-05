package com.canerture.home.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val quizCount: Int,
)
