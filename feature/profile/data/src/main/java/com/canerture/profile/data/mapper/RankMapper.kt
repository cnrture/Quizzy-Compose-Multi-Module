package com.canerture.profile.data.mapper

import com.canerture.core.common.orZero
import com.canerture.profile.data.model.RankResponse
import com.canerture.profile.domain.model.RankModel

internal fun RankResponse?.toModel(): RankModel {
    return RankModel(
        rank = this?.rank.orZero().toString(),
        score = this?.score.orZero().toString(),
    )
}