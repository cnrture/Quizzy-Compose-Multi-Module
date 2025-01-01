package com.canerture.network.model

import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse<T>(
    val data: T,
    val message: String,
)