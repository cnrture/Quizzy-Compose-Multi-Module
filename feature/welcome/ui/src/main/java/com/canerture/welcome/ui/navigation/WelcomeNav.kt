package com.canerture.welcome.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.welcome.ui.WelcomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeRoute

fun NavGraphBuilder.welcomeScreen(
    onNavigateLogin: () -> Unit,
    onNavigateRegister: () -> Unit,
) {
    composable<WelcomeRoute> {
        WelcomeScreen(
            onNavigateLogin = onNavigateLogin,
            onNavigateRegister = onNavigateRegister,
        )
    }
}