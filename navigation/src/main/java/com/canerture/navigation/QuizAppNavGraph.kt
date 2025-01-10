package com.canerture.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.canerture.detail.ui.navigation.Detail
import com.canerture.detail.ui.navigation.detailScreen
import com.canerture.favorites.ui.navigation.Favorites
import com.canerture.favorites.ui.navigation.favoritesScreen
import com.canerture.home.ui.navigation.Home
import com.canerture.home.ui.navigation.homeScreen
import com.canerture.leaderboard.ui.navigation.Leaderboard
import com.canerture.leaderboard.ui.navigation.leaderboardScreen
import com.canerture.login.ui.navigation.Login
import com.canerture.login.ui.navigation.loginScreen
import com.canerture.profile.ui.navigation.Profile
import com.canerture.profile.ui.navigation.profileScreen
import com.canerture.register.ui.navigation.Register
import com.canerture.register.ui.navigation.registerScreen
import com.canerture.ui.navigation.Splash
import com.canerture.ui.navigation.splashScreen
import com.canerture.ui.theme.QuizAppTheme
import com.canerture.welcome.ui.navigation.Welcome
import com.canerture.welcome.ui.navigation.welcomeScreen
import kotlinx.serialization.Serializable

@Composable
fun QuizAppNavGraph() {
    val navController = rememberNavController()

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    val visibleBottomSheetScreen = listOf(
        Home::class.java.canonicalName,
        Favorites::class.java.canonicalName,
        Leaderboard::class.java.canonicalName,
        Profile::class.java.canonicalName,
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val bottomBarVisibility = currentRoute in visibleBottomSheetScreen

    LaunchedEffect(Unit) {
        navController.currentBackStackEntryFlow.collect {
            selectedItem = when (it.destination.route) {
                Home::class.java.canonicalName -> 0
                Favorites::class.java.canonicalName -> 1
                Leaderboard::class.java.canonicalName -> 2
                Profile::class.java.canonicalName -> 3
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
                AnimatedVisibility(bottomBarVisibility) {
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
        startDestination = LoginFlow,
        modifier = modifier,
    ) {
        loginFlowNavigation(navController)
        homeScreen(
            onNavigateDetail = { navController.navigate(Detail(it)) }
        )
        favoritesScreen()
        leaderboardScreen()
        profileScreen()
        detailScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateQuiz = {}
        )
    }
}

fun NavGraphBuilder.loginFlowNavigation(navController: NavHostController) {
    navigation<LoginFlow>(Splash) {
        splashScreen(
            onNavigateWelcome = { navController.navigateWithPopUpTo(Welcome, Splash) },
            onNavigateHome = { navController.navigateWithPopUpTo(Home, LoginFlow) }
        )
        welcomeScreen(
            onNavigateLogin = { navController.navigate(Login) },
            onNavigateRegister = { navController.navigate(Register) },
            onNavigateHome = { navController.navigateWithPopUpTo(Home, LoginFlow) }
        )
        loginScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateRegister = { navController.navigate(Register) },
            onNavigateHome = { navController.navigateWithPopUpTo(Home, LoginFlow) }
        )
        registerScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateLogin = { navController.navigateWithPopUpTo(Login, Register) }
        )
    }
}

fun NavHostController.navigateWithPopUpTo(screen: Any, popUp: Any) {
    navigate(screen) {
        popUpTo(popUp) { inclusive = true }
    }
}

@Serializable
object LoginFlow