package com.canerture.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.canerture.login.ui.navigation.Login
import com.canerture.login.ui.navigation.loginScreen
import com.canerture.register.ui.navigation.Register
import com.canerture.register.ui.navigation.registerScreen
import com.canerture.ui.navigation.Screen
import com.canerture.welcome.ui.navigation.Welcome
import com.canerture.welcome.ui.navigation.welcomeScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginFlow : Screen

fun NavGraphBuilder.loginFlowNavigation(navController: NavHostController) {
    navigation<LoginFlow>(Welcome) {
        welcomeScreen(
            onNavigateLogin = { navController.navigate(Login) },
            onNavigateRegister = { navController.navigate(Register) },
            onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, LoginFlow) }
        )
        loginScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateRegister = { navController.navigate(Register) },
            onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, LoginFlow) }
        )
        registerScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateLogin = { navController.navigateWithPopUpTo(Login, Register) }
        )
    }
}