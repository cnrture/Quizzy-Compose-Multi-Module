package com.canerture.home.data.mapper

import com.canerture.core.common.orZero
import com.canerture.home.data.model.CategoryResponse
import com.canerture.home.domain.model.CategoryModel

fun List<CategoryResponse>?.toModel(): List<CategoryModel> {
    return this?.map {
        CategoryModel(
            id = it.id.orZero(),
            name = it.name.orEmpty(),
            imageUrl = it.imageUrl.orEmpty(),
            quizCount = it.quizCount.orZero()
        )
    }.orEmpty()
}