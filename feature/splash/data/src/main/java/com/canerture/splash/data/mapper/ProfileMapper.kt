package com.canerture.splash.data.mapper

import com.canerture.datasource.profile.ProfileModel
import com.canerture.splash.data.model.UserResponse

fun UserResponse?.toModel(): ProfileModel {
    return ProfileModel(
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
        avatarUrl = this?.avatarUrl.orEmpty()
    )
}