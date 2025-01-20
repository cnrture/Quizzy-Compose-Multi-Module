package com.canerture.welcome.data.mapper

import com.canerture.datasource.profile.ProfileModel
import com.canerture.welcome.data.model.UserResponse

internal fun UserResponse?.toModel() = ProfileModel(
    email = this?.email.orEmpty(),
    username = this?.username.orEmpty(),
    avatarUrl = this?.avatarUrl.orEmpty(),
)