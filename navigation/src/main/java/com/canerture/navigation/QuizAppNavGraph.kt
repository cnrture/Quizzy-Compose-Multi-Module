package com.canerture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.canerture.ui.navigation.Splash
import com.canerture.ui.navigation.splashScreen

@Composable
fun QuizAppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Splash,
        modifier = modifier,
    ) {
        splashScreen(
            onNavigateWelcome = { navController.navigateWithPopUpTo(LoginFlow, Splash) },
            onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, Splash) }
        )
        loginFlowNavigation(navController)
        mainFlowNavigation(navController)
    }
}