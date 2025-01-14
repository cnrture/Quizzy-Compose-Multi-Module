package com.canerture.profile.data.mapper

import com.canerture.profile.data.model.ProfileResponse
import com.canerture.profile.domain.model.ProfileModel

fun com.canerture.datasource.profile.ProfileModel.toModel(): ProfileModel {
    return ProfileModel(
        email = email,
        username = username,
    )
}

fun ProfileResponse?.toModel(): ProfileModel {
    return ProfileModel(
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
    )
}

fun ProfileModel.toModel(): com.canerture.datasource.profile.ProfileModel {
    return com.canerture.datasource.profile.ProfileModel(
        email = email,
        username = username,
    )
}