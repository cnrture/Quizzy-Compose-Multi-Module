package com.canerture.category.data.mapper

import com.canerture.category.data.model.QuizResponse
import com.canerture.category.domain.model.QuizModel
import com.canerture.core.common.orZero

internal fun List<QuizResponse>?.toModel(): List<QuizModel> {
    return this?.map {
        QuizModel(
            id = it.id.orZero(),
            name = it.name.orEmpty(),
            questionCount = it.questionCount.orZero(),
            imageUrl = it.imageUrl.orEmpty()
        )
    }.orEmpty()
}