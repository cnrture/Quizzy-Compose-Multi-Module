package com.canerture.quiz.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResponse(
    val id: Int? = null,
    val question: String? = null,
    val categoryId: Int? = null,
    val options: List<String>? = null,
    val answer: String? = null,
)
