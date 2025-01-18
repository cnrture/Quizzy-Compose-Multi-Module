package com.canerture.detail.domain.model

data class QuizDetailModel(
    val id: Int,
    val name: String,
    val score: Int,
    val questionCountStr: String,
    val favoriteCountStr: String,
    val playedCountStr: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val isFavorite: Boolean,
)