package com.canerture.editprofile.data.mapper

import com.canerture.editprofile.data.model.ProfileResponse
import com.canerture.editprofile.domain.model.ProfileModel

internal fun com.canerture.datasource.profile.ProfileModel.toModel(): ProfileModel {
    return ProfileModel(
        email = email,
        username = username,
        avatarUrl = avatarUrl,
    )
}

internal fun ProfileResponse?.toModel(): com.canerture.datasource.profile.ProfileModel {
    return com.canerture.datasource.profile.ProfileModel(
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
        avatarUrl = this?.avatarUrl.orEmpty(),
    )
}