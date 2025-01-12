package com.canerture.datasource.logout

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
class LogoutDataSource {

    private val isLogout = MutableSharedFlow<Boolean?>(1)

    init {
        isLogout.tryEmit(null)
    }

    fun save(value: Boolean?) = isLogout.tryEmit(value)

    fun get(): Flow<Boolean?> = isLogout.asSharedFlow()
}