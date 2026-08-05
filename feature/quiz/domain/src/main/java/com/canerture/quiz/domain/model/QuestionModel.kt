package com.canerture.quiz.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class QuestionModel(
    val question: String,
    val options: List<OptionModel>,
    val answer: String,
)
