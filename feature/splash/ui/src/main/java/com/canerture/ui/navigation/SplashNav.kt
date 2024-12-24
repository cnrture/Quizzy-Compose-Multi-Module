package com.canerture.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.ui.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavGraphBuilder.splashScreen() {
    composable<SplashRoute> {
        SplashScreen()
    }
}