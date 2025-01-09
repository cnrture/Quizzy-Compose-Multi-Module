package com.canerture.leaderboard.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.leaderboard.ui.LeaderboardScreen
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Leaderboard : Screen

fun NavGraphBuilder.leaderboardScreen() {
    composable<Leaderboard> {
        LeaderboardScreen()
    }
}