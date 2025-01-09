package com.canerture.home.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.ui.navigation.Screen
import com.canerture.home.ui.HomeScreen
import com.canerture.home.ui.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Home : Screen

fun NavGraphBuilder.homeScreen() {
    composable<Home> {
        val viewModel = hiltViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        HomeScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
        )
    }
}