package com.canerture.quiz.data.mapper

import com.canerture.core.common.orZero
import com.canerture.quiz.data.model.QuestionResponse
import com.canerture.quiz.data.model.QuizResponse
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.OptionState
import com.canerture.quiz.domain.model.QuestionModel
import com.canerture.quiz.domain.model.QuizModel

internal fun QuizResponse?.toModel(): QuizModel {
    return QuizModel(
        id = this?.id.orZero(),
        categoryId = this?.categoryId.orZero(),
        score = this?.score.orZero(),
        questions = this?.questions.toQuestionModel(),
    )
}

internal fun List<QuestionResponse>?.toQuestionModel(): List<QuestionModel> {
    return this?.map {
        QuestionModel(
            question = it.question.orEmpty(),
            answer = it.answer.orEmpty(),
            options = it.options.toModel(),
        )
    }.orEmpty()
}

internal fun List<String>?.toModel(): List<OptionModel> {
    return this?.map {
        OptionModel(
            option = it,
            state = OptionState.UNSELECTED,
        )
    }.orEmpty()
}