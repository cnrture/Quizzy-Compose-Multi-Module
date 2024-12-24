package com.canerture.core.common

fun Int?.orZero() = this ?: 0

fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false