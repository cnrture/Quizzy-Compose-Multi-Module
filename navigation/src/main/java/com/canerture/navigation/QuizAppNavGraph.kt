package com.canerture.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.canerture.ui.navigation.Splash
import com.canerture.ui.navigation.splashScreen
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppNavGraph(
    navController: NavHostController,
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    val visibleBottomSheetScreen = NavigationItem.getNavigationRoutes()

    val bottomBarVisibility =
        navController.currentBackStackEntryAsState().value?.destination?.route in visibleBottomSheetScreen

    LaunchedEffect(Unit) {
        navController.currentBackStackEntryFlow.collect {
            selectedItem = when (navController.currentDestination?.route) {
                NavigationItem.HomeScreen.route.getRoute() -> 0
                NavigationItem.FavoritesScreen.route.getRoute() -> 1
                NavigationItem.LeaderboardScreen.route.getRoute() -> 2
                NavigationItem.ProfileScreen.route.getRoute() -> 3
                else -> selectedItem
            }
        }
    }

    QuizAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = QuizAppTheme.colors.background,
            content = { innerPadding ->
                NavGraph(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    navController = navController,
                )
            },
            bottomBar = {
                if (bottomBarVisibility) {
                    Column {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = QuizAppTheme.colors.black,
                        )
                        QuizAppBottomBar(
                            navController = navController,
                            selectedItem = selectedItem,
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun NavGraph(
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