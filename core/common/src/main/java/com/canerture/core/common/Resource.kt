package com.canerture.core.common

sealed class Resource<out T> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Fail(val message: String) : Resource<Nothing>()
}

inline fun <T> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

inline fun <T> Resource<T>.onFailure(action: (String) -> Unit): Resource<T> {
    if (this is Resource.Fail) action(message)
    return this
}

inline fun <T, R : Any> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Fail -> Resource.Fail(message)
    }
}