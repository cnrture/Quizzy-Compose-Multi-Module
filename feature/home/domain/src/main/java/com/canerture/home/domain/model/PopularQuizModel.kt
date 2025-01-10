package com.canerture.home.domain.model

data class PopularQuizModel(
    val id: Int,
    val category: String,
    val name: String,
    val questionCount: Int,
    val imageUrl: String,
)
