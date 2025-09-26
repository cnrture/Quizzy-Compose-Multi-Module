package com.canerture.favorites.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.favorites.ui.FavoritesScreen
import com.canerture.favorites.ui.FavoritesViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Favorites : Screen

fun NavGraphBuilder.favoritesScreen(
    onNavigateDetail: (Int) -> Unit,
) {
    composable<Favorites> {
        val viewModel = hiltViewModel<FavoritesViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        FavoritesScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateDetail = onNavigateDetail
        )
    }
}