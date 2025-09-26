package com.canerture.detail.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.detail.ui.DetailScreen
import com.canerture.detail.ui.DetailViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data class Detail(val id: Int) : Screen

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit
) {
    composable<Detail> {
        val viewModel = hiltViewModel<DetailViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        DetailScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateQuiz = onNavigateQuiz
        )
    }
}