package com.canerture.search.data.mapper

import com.canerture.core.common.orZero
import com.canerture.search.data.model.QuizResponse
import com.canerture.search.domain.model.QuizModel

fun List<QuizResponse>?.toModel(): List<QuizModel> {
    return this?.map {
        QuizModel(
            id = it.id.orZero(),
            name = it.name.orEmpty(),
            questionCount = it.questionCount.orZero(),
            imageUrl = it.imageUrl.orEmpty(),
            category = it.category.orEmpty()
        )
    }.orEmpty()
}