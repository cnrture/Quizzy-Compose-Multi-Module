package com.canerture.login.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.login.ui.LoginScreen
import com.canerture.login.ui.LoginViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Login : Screen

fun NavGraphBuilder.loginScreen(
    onNavigateBack: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    composable<Login> {
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