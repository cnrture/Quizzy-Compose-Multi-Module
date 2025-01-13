package com.canerture.result.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.result.ui.SummaryScreen
import com.canerture.result.ui.SummaryViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    val quizId: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val score: Int,
) : Screen

fun NavGraphBuilder.summaryScreen(
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit,
) {
    composable<Summary> {
        val viewModel = hiltViewModel<SummaryViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        SummaryScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateQuiz = onNavigateQuiz,
        )
    }
}