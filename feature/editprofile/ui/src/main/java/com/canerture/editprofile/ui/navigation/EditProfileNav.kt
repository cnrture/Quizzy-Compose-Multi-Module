package com.canerture.editprofile.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.editprofile.ui.EditProfileScreen
import com.canerture.editprofile.ui.EditProfileViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object EditProfile : Screen

fun NavGraphBuilder.editProfileScreen(
    onNavigateBack: () -> Unit,
) {
    composable<EditProfile> {
        val viewModel = hiltViewModel<EditProfileViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        EditProfileScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateBack = onNavigateBack,
        )
    }
}