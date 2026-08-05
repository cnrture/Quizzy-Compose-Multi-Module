package com.canerture.quiz.domain.usecase

import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.OptionState
import javax.inject.Inject

class UpdateOptionsUseCase @Inject constructor() {

    operator fun invoke(
        options: List<OptionModel>,
        answer: String,
        selectedOption: OptionModel? = null,
    ): Pair<List<OptionModel>, Boolean> {
        val correctId = options.firstOrNull { it.option == answer }?.id
        val isCorrect = selectedOption == null || selectedOption.id == correctId
        val updatedOptions = options.map {
            when {
                it.id == correctId -> it.copy(state = OptionState.CORRECT)
                it.id == selectedOption?.id -> it.copy(state = OptionState.INCORRECT)
                else -> it
            }
        }

        return Pair(updatedOptions, isCorrect)
    }
}