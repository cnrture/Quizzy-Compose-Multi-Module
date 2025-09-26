package com.canerture.search.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.search.ui.SearchScreen
import com.canerture.search.ui.SearchViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Search : Screen

fun NavGraphBuilder.searchScreen(
    onNavigateBack: () -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    composable<Search> {
        val viewModel = hiltViewModel<SearchViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        SearchScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateDetail = onNavigateDetail,
        )
    }
}