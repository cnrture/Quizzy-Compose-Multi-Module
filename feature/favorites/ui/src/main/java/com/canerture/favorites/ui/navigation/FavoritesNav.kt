package com.canerture.favorites.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.favorites.ui.FavoritesScreen
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Favorites : Screen

fun NavGraphBuilder.favoritesScreen() {
    composable<Favorites> {
        FavoritesScreen()
    }
}