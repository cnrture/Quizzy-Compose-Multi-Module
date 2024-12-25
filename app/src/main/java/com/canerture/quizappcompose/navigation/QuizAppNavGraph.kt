package com.canerture.quizappcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.canerture.login.ui.navigation.LoginRoute
import com.canerture.login.ui.navigation.loginScreen
import com.canerture.register.ui.navigation.RegisterRoute
import com.canerture.register.ui.navigation.registerScreen
import com.canerture.ui.navigation.SplashRoute
import com.canerture.ui.navigation.splashScreen
import com.canerture.welcome.ui.navigation.WelcomeRoute
import com.canerture.welcome.ui.navigation.welcomeScreen

@Composable
fun QuizAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
    ) {
        splashScreen(
            onNavigateWelcome = { navController.navigate(WelcomeRoute) }
        )
        welcomeScreen(
            onNavigateLogin = { navController.navigate(LoginRoute) },
            onNavigateRegister = { navController.navigate(RegisterRoute) }
        )
        loginScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateRegister = { navController.navigate(RegisterRoute) },
            onNavigateHome = {
                //navController.navigate(WelcomeRoute)
            }
        )
        registerScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}