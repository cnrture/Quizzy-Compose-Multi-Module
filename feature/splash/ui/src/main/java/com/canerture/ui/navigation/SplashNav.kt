package com.canerture.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.ui.SplashScreen
import com.canerture.ui.SplashViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Splash : Screen

fun NavGraphBuilder.splashScreen(
    onNavigateWelcome: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    composable<Splash> {
        val viewModel = hiltViewModel<SplashViewModel>()
        val uiEffect = viewModel.uiEffect
        SplashScreen(
            uiEffect = uiEffect,
            onNavigateWelcome = onNavigateWelcome,
            onNavigateHome = onNavigateHome,
        )
    }
}