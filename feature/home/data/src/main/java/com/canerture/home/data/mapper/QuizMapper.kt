package com.canerture.home.data.mapper

import com.canerture.core.common.orZero
import com.canerture.home.data.model.PopularQuizzesResponse
import com.canerture.home.domain.model.PopularQuizModel

fun List<PopularQuizzesResponse>?.toModel(): List<PopularQuizModel> {
    return this?.map {
        PopularQuizModel(
            id = it.id.orZero(),
            category = it.category?.name.orEmpty(),
            name = it.name.orEmpty(),
            questionCount = it.questionCount.orZero(),
            imageUrl = it.imageUrl.orEmpty()
        )
    }.orEmpty()
}