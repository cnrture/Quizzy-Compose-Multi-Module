package com.canerture.profile.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.profile.ui.ProfileScreen
import com.canerture.profile.ui.ProfileViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Profile : Screen

fun NavGraphBuilder.profileScreen(
    onNavigateEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    composable<Profile> {
        val viewModel = hiltViewModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        ProfileScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            onNavigateEditProfile = onNavigateEditProfile,
            onLogout = onLogout,
        )
    }
}