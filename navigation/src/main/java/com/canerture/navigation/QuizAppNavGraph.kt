package com.canerture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.canerture.home.ui.navigation.HomeRoute
import com.canerture.home.ui.navigation.homeScreen
import com.canerture.login.ui.navigation.LoginRoute
import com.canerture.login.ui.navigation.loginScreen
import com.canerture.register.ui.navigation.RegisterRoute
import com.canerture.register.ui.navigation.registerScreen
import com.canerture.ui.navigation.SplashRoute
import com.canerture.ui.navigation.splashScreen
import com.canerture.welcome.ui.navigation.WelcomeRoute
import com.canerture.welcome.ui.navigation.welcomeScreen
import kotlinx.serialization.Serializable

@Composable
fun QuizAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = LoginFlowRoute,
        modifier = modifier,
    ) {
        loginFlowNavigation(navController)
        homeScreen()
    }
}

fun NavGraphBuilder.loginFlowNavigation(navController: NavHostController) {
    navigation<LoginFlowRoute>(WelcomeRoute) {
        splashScreen(
            onNavigateWelcome = { navController.navigateWithPopUpTo(WelcomeRoute, SplashRoute) },
            onNavigateHome = { navController.navigateWithPopUpTo(HomeRoute, LoginFlowRoute) }
        )
        welcomeScreen(
            onNavigateLogin = { navController.navigate(LoginRoute) },
            onNavigateRegister = { navController.navigate(RegisterRoute) },
            onNavigateHome = { navController.navigateWithPopUpTo(HomeRoute, LoginFlowRoute) }
        )
        loginScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateRegister = { navController.navigate(RegisterRoute) },
            onNavigateHome = { navController.navigateWithPopUpTo(HomeRoute, LoginFlowRoute) }
        )
        registerScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateLogin = { navController.navigateWithPopUpTo(LoginRoute, RegisterRoute) }
        )
    }
}

fun NavHostController.navigateWithPopUpTo(route: Any, popUpRoute: Any) {
    navigate(route) {
        popUpTo(popUpRoute) { inclusive = true }
    }
}

@Serializable
object LoginFlowRoute