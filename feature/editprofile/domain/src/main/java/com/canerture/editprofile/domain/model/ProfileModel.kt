package com.canerture.editprofile.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileModel(
    val email: String,
    val username: String,
    val avatarUrl: String,
)
