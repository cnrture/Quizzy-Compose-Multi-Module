package com.canerture.home.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.home.ui.HomeScreen
import com.canerture.home.ui.HomeViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Home : Screen

fun NavGraphBuilder.homeScreen(
    onNavigateSearch: () -> Unit,
    onNavigateDetail: (Int) -> Unit,
    onNavigateCategory: (Int, String, String) -> Unit,
) {
    composable<Home> {
        val viewModel = hiltViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        HomeScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateSearch = onNavigateSearch,
            onNavigateDetail = onNavigateDetail,
            onNavigateCategory = onNavigateCategory
        )
    }
}