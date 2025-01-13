package com.canerture.quiz.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubmitQuizRequest(
    val quizId: Int,
    val score: Int,
)
