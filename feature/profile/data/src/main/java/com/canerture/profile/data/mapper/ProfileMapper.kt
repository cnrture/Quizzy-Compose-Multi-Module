package com.canerture.profile.data.mapper

import com.canerture.profile.data.model.ProfileResponse
import com.canerture.profile.domain.model.ProfileModel

internal fun com.canerture.datasource.profile.ProfileModel.toModel(): ProfileModel {
    return ProfileModel(
        email = email,
        username = username,
        avatarUrl = avatarUrl,
    )
}

internal fun ProfileResponse?.toModel(): ProfileModel {
    return ProfileModel(
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
        avatarUrl = this?.avatarUrl.orEmpty(),
    )
}

internal fun ProfileModel.toModel(): com.canerture.datasource.profile.ProfileModel {
    return com.canerture.datasource.profile.ProfileModel(
        email = email,
        username = username,
        avatarUrl = avatarUrl,
    )
}