package com.canerture.quiz.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class QuizResponse(
    val id: Int? = null,
    val score: Int? = null,
    val categoryId: Int? = null,
    val questions: List<QuestionResponse>? = null,
)

@Serializable
internal data class QuestionResponse(
    val question: String? = null,
    val options: List<String>? = null,
    val answer: String? = null,
)
