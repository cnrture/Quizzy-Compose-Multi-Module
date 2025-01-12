package com.canerture.quiz.domain.model

data class QuestionModel(
    val question: String,
    val options: List<OptionModel>,
    val answer: String,
)
