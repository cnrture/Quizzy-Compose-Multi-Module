package com.canerture.quiz.domain.model

data class QuizModel(
    val id: Int,
    val categoryId: Int,
    val questions: List<QuestionModel>,
)
