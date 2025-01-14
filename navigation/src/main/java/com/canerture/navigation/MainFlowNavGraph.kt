package com.canerture.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.canerture.detail.ui.navigation.Detail
import com.canerture.detail.ui.navigation.detailScreen
import com.canerture.favorites.ui.navigation.favoritesScreen
import com.canerture.home.ui.navigation.Home
import com.canerture.home.ui.navigation.homeScreen
import com.canerture.leaderboard.ui.navigation.leaderboardScreen
import com.canerture.profile.ui.navigation.profileScreen
import com.canerture.quiz.ui.navigation.Quiz
import com.canerture.quiz.ui.navigation.quizScreen
import com.canerture.result.ui.navigation.Summary
import com.canerture.result.ui.navigation.summaryScreen
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
object MainFlow : Screen

fun NavGraphBuilder.mainFlowNavigation(navController: NavHostController) {
    navigation<MainFlow>(Home) {
        homeScreen(
            onNavigateDetail = { navController.navigate(Detail(it)) }
        )
        favoritesScreen()
        leaderboardScreen()
        profileScreen(
            onNavigateEditProfile = {}
        )
        detailScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateQuiz = { navController.navigate(Quiz(it)) }
        )
        quizScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateSummary = { quizId, correctAnswers, wrongAnswers, score ->
                navController.navigateWithPopUpTo(Summary(quizId, correctAnswers, wrongAnswers, score), Quiz(quizId))
            }
        )
        summaryScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateQuiz = {}
        )
    }
}