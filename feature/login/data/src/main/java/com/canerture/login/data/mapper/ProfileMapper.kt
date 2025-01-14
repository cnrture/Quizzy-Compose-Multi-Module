package com.canerture.login.data.mapper

import com.canerture.datasource.profile.ProfileModel
import com.canerture.login.data.model.UserResponse

fun UserResponse?.toModel(): ProfileModel {
    return ProfileModel(
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
        avatarUrl = this?.avatarUrl.orEmpty()
    )
}