package com.canerture.welcome.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.ui.navigation.Screen
import com.canerture.welcome.ui.WelcomeScreen
import com.canerture.welcome.ui.WelcomeViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Welcome : Screen

fun NavGraphBuilder.welcomeScreen(
    onNavigateLogin: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    composable<Welcome> {
        val viewModel = hiltViewModel<WelcomeViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        WelcomeScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateLogin = onNavigateLogin,
            onNavigateRegister = onNavigateRegister,
            onNavigateHome = onNavigateHome,
        )
    }
}