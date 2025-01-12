package com.canerture.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityListener {
    val isNetworkAvailable: Flow<Boolean>
}