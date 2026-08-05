package com.canerture.category.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class QuizModel(
    val id: Int,
    val name: String,
    val questionCount: Int,
    val imageUrl: String,
)
