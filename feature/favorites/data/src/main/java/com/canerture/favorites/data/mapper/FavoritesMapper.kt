package com.canerture.favorites.data.mapper

import com.canerture.core.common.orZero
import com.canerture.favorites.data.model.FavoriteResponse
import com.canerture.favorites.domain.model.FavoriteModel

internal fun List<FavoriteResponse>?.toModel() = this?.map {
    FavoriteModel(
        id = it.id.orZero(),
        name = it.name.orEmpty(),
        category = it.category.orEmpty(),
        questionCount = it.questionCount.orZero(),
        playedCount = it.playedCount.orZero(),
        imageUrl = it.imageUrl.orEmpty(),
    )
}.orEmpty()