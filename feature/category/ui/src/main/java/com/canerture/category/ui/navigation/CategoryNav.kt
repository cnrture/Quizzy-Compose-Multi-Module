package com.canerture.category.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.category.ui.CategoryScreen
import com.canerture.category.ui.CategoryViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val imageUrl: String,
) : Screen

fun NavGraphBuilder.categoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateDetail: (Int) -> Unit,
) {
    composable<Category> {
        val viewModel = hiltViewModel<CategoryViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        CategoryScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
            onNavigateDetail = onNavigateDetail
        )
    }
}