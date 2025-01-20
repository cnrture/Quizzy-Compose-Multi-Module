package com.canerture.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

internal object PreferencesKeys {
    const val STORE_NAME = "quiz_data_store"
    val TOKEN = stringPreferencesKey("token")
}