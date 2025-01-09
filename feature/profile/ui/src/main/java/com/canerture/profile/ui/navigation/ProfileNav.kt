package com.canerture.profile.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.profile.ui.ProfileScreen
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Profile : Screen

fun NavGraphBuilder.profileScreen() {
    composable<Profile> {
        ProfileScreen()
    }
}