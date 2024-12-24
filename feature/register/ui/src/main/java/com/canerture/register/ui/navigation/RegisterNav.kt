package com.canerture.register.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.register.ui.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
data object RegisterRoute

fun NavGraphBuilder.registerScreen(
    onNavigateBack: () -> Unit,
    onNavigateLogin: () -> Unit,
) {
    composable<RegisterRoute> {
        RegisterScreen(
            onNavigateBack = onNavigateBack,
            onNavigateLogin = onNavigateLogin,
        )
    }
}