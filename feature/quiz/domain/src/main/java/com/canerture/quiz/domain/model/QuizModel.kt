package com.canerture.quiz.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class QuizModel(
    val id: Int,
    val categoryId: Int,
    val score: Int,
    val questions: List<QuestionModel>,
)
