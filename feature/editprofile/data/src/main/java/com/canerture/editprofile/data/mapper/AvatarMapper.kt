package com.canerture.editprofile.data.mapper

import com.canerture.core.common.orZero
import com.canerture.editprofile.data.model.AvatarResponse
import com.canerture.editprofile.domain.model.AvatarModel

internal fun List<AvatarResponse>?.toModel(): List<AvatarModel> {
    return this?.map {
        AvatarModel(
            id = it.id.orZero(),
            url = it.url.orEmpty(),
        )
    }.orEmpty()
}