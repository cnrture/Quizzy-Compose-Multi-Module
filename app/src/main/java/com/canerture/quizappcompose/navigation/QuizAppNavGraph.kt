package com.canerture.quizappcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
        splashScreen(onNavigateWelcome = { navController.navigate(WelcomeRoute) })
        welcomeScreen(
            onNavigateLogin = {
                //navController.navigate(WelcomeRoute)
            },
            onNavigateRegister = {
                //navController.navigate(WelcomeRoute)
            }
        )
    }
}