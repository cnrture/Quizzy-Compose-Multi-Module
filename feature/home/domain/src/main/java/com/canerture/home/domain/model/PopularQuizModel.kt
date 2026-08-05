package com.canerture.home.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PopularQuizModel(
    val id: Int,
    val category: String,
    val name: String,
    val questionCount: Int,
    val imageUrl: String,
)
