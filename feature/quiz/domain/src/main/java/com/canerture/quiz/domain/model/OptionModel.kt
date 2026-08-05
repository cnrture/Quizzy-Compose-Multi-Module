package com.canerture.quiz.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class OptionModel(
    val option: String,
    val state: OptionState,
)
