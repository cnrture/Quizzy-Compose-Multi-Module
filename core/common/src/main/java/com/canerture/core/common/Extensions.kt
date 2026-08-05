package com.canerture.core.common

fun Int?.orZero() = this ?: 0

fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false

fun <T : Any> Result<T>.toUnit(): Result<Unit> {
    return this.map {}
}

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

fun String.isValidEmail(): Boolean = EMAIL_REGEX.matches(this)