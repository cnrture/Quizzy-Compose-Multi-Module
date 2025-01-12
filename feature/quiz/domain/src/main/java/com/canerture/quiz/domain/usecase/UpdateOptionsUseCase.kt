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
        val correctOption = options.find { it.option == answer }
        var isCorrect = true
        val updatedOptions = options.map {
            when {
                selectedOption == null && it.option == correctOption?.option -> {
                    it.copy(state = OptionState.CORRECT)
                }

                it.option != correctOption?.option && it.option == selectedOption?.option -> {
                    isCorrect = false
                    it.copy(state = OptionState.INCORRECT)
                }

                it.option == correctOption?.option -> {
                    it.copy(state = OptionState.CORRECT)
                }

                else -> it
            }
        }

        return Pair(updatedOptions, isCorrect)
    }
}