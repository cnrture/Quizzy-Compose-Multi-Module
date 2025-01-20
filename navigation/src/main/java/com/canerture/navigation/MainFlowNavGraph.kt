package com.canerture.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.canerture.category.ui.navigation.Category
import com.canerture.category.ui.navigation.categoryScreen
import com.canerture.detail.ui.navigation.Detail
import com.canerture.detail.ui.navigation.detailScreen
import com.canerture.editprofile.ui.navigation.EditProfile
import com.canerture.editprofile.ui.navigation.editProfileScreen
import com.canerture.favorites.ui.navigation.favoritesScreen
import com.canerture.home.ui.navigation.Home
import com.canerture.home.ui.navigation.homeScreen
import com.canerture.leaderboard.ui.navigation.leaderboardScreen
import com.canerture.profile.ui.navigation.profileScreen
import com.canerture.quiz.ui.navigation.Quiz
import com.canerture.quiz.ui.navigation.quizScreen
import com.canerture.result.ui.navigation.Summary
import com.canerture.result.ui.navigation.summaryScreen
import com.canerture.search.ui.navigation.Search
import com.canerture.search.ui.navigation.searchScreen
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
object MainFlow : Screen

internal fun NavGraphBuilder.mainFlowNavigation(navController: NavHostController) {
    navigation<MainFlow>(Home) {
        homeScreen(
            onNavigateSearch = { navController.navigate(Search) },
            onNavigateDetail = { navController.navigate(Detail(it)) },
            onNavigateCategory = { id, name, imageUrl ->
                navController.navigate(Category(id, name, imageUrl))
            },
        )
        categoryScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateDetail = { navController.navigate(Detail(it)) }
        )
        searchScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateDetail = { navController.navigate(Detail(it)) }
        )
        favoritesScreen(
            onNavigateDetail = { navController.navigate(Detail(it)) }
        )
        leaderboardScreen()
        profileScreen(
            onNavigateEditProfile = { navController.navigate(EditProfile) },
            onLogout = { navController.navigateWithPopUpTo(LoginFlow, MainFlow) }
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
            onNavigateQuiz = { navController.navigate(Quiz(it)) }
        )
        editProfileScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}