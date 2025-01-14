package com.canerture.editprofile.data.mapper

import com.canerture.editprofile.data.model.ProfileResponse
import com.canerture.editprofile.domain.model.ProfileModel

fun com.canerture.datasource.profile.ProfileModel.toModel(): ProfileModel {
    return ProfileModel(
        email = email,
        username = username,
        avatarUrl = avatarUrl,
    )
}

fun ProfileResponse.toModel(): com.canerture.datasource.profile.ProfileModel {
    return com.canerture.datasource.profile.ProfileModel(
        email = email.orEmpty(),
        username = username.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
    )
}