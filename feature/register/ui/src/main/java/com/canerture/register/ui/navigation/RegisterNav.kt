package com.canerture.register.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.register.ui.RegisterScreen
import com.canerture.register.ui.RegisterViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Register : Screen

fun NavGraphBuilder.registerScreen(
    onNavigateBack: () -> Unit,
    onNavigateLogin: () -> Unit,
) {
    composable<Register> {
        val viewModel: RegisterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        RegisterScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateLogin = onNavigateLogin,
        )
    }
}