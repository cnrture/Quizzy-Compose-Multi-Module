package com.canerture.search.domain.model

data class QuizModel(
    val id: Int,
    val name: String,
    val category: String,
    val questionCount: Int,
    val playedCount: Int,
    val imageUrl: String,
)