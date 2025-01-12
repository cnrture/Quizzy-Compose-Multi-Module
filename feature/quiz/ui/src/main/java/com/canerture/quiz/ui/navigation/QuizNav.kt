package com.canerture.quiz.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.quiz.ui.QuizScreen
import com.canerture.quiz.ui.QuizViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data class Quiz(val id: Int) : Screen

fun NavGraphBuilder.quizScreen(
    onNavigateBack: () -> Unit,
    onNavigateResult: (Int) -> Unit,
) {
    composable<Quiz> {
        val viewModel = hiltViewModel<QuizViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        QuizScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateResult = onNavigateResult,
        )
    }
}