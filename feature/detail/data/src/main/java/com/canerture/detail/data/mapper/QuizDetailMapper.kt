package com.canerture.detail.data.mapper

import com.canerture.core.common.orZero
import com.canerture.detail.data.model.QuizDetailResponse
import com.canerture.detail.domain.model.QuizDetailModel

fun QuizDetailResponse?.toModel(): QuizDetailModel {
    return QuizDetailModel(
        id = this?.id.orZero(),
        name = this?.name.orEmpty(),
        score = this?.score.orZero(),
        questionCountStr = this?.questionCount.orZero().toString(),
        favoriteCountStr = this?.favoriteCount.orZero().toString(),
        playedCountStr = this?.playedCount.orZero().toString(),
        description = this?.description.orEmpty(),
        imageUrl = this?.imageUrl.orEmpty(),
        category = this?.category.orEmpty(),
    )
}