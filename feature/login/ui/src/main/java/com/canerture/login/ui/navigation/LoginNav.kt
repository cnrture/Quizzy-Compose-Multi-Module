package com.canerture.login.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.login.ui.LoginScreen
import com.canerture.login.ui.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavGraphBuilder.loginScreen(
    onNavigateBack: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    composable<LoginRoute> {
        val viewModel = hiltViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        LoginScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateRegister = onNavigateRegister,
            onNavigateHome = onNavigateHome,
        )
    }
}