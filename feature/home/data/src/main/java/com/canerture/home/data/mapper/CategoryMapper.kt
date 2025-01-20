package com.canerture.home.data.mapper

import com.canerture.core.common.orZero
import com.canerture.home.data.model.CategoriesResponse
import com.canerture.home.domain.model.CategoryModel

internal fun List<CategoriesResponse>?.toModel(): List<CategoryModel> {
    return this?.map {
        CategoryModel(
            id = it.id.orZero(),
            name = it.name.orEmpty(),
            imageUrl = it.imageUrl.orEmpty(),
            quizCount = it.quizCount.orZero()
        )
    }.orEmpty()
}