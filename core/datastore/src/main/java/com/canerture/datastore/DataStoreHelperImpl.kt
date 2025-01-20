package com.canerture.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.canerture.datastore.PreferencesKeys.TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStoreHelperImpl @Inject constructor(private val context: Context) : DataStoreHelper {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PreferencesKeys.STORE_NAME)

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    override fun getToken(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}