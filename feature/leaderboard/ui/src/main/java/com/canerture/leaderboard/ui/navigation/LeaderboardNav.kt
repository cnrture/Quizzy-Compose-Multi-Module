package com.canerture.leaderboard.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.leaderboard.ui.LeaderboardScreen
import com.canerture.leaderboard.ui.LeaderboardViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Leaderboard : Screen

fun NavGraphBuilder.leaderboardScreen() {
    composable<Leaderboard> {
        val viewModel = hiltViewModel<LeaderboardViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        LeaderboardScreen(
            uiState = uiState,
            uiEffect = uiEffect,
        )
    }
}