package com.canerture.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreHelper {
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String>
    suspend fun clear()
}