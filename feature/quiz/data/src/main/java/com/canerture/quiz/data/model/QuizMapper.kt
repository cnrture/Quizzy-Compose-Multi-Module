package com.canerture.quiz.data.model

import com.canerture.core.common.orZero
import com.canerture.quiz.data.mapper.QuizResponse
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.OptionState
import com.canerture.quiz.domain.model.QuestionModel
import com.canerture.quiz.domain.model.QuizModel

fun List<QuizResponse>?.toModel(): QuizModel {
    val questions = this.toQuestionModel()
    return QuizModel(
        id = this?.firstOrNull()?.id.orZero(),
        categoryId = this?.firstOrNull()?.categoryId.orZero(),
        questions = questions,
    )
}

fun List<QuizResponse>?.toQuestionModel(): List<QuestionModel> {
    return this?.map {
        QuestionModel(
            question = it.question.orEmpty(),
            answer = it.answer.orEmpty(),
            options = it.options.toModel(),
        )
    }.orEmpty()
}

fun List<String>?.toModel(): List<OptionModel> {
    return this?.map {
        OptionModel(
            option = it,
            state = OptionState.UNSELECTED,
        )
    }.orEmpty()
}