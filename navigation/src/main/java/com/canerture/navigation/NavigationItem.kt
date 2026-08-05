package com.canerture.navigation

import com.canerture.favorites.ui.navigation.Favorites
import com.canerture.home.ui.navigation.Home
import com.canerture.leaderboard.ui.navigation.Leaderboard
import com.canerture.profile.ui.navigation.Profile
import com.canerture.quiz.navigation.R
import com.canerture.ui.navigation.Screen

sealed class NavigationItem(
    val route: Screen,
    val title: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val testTagKey: String,
) {
    data object HomeScreen : NavigationItem(
        route = Home,
        title = R.string.home,
        selectedIcon = R.drawable.ic_home_selected,
        unselectedIcon = R.drawable.ic_home_unselected,
        testTagKey = BottomBarTestTags.HOME,
    )

    data object FavoritesScreen : NavigationItem(
        route = Favorites,
        title = R.string.favorites,
        selectedIcon = R.drawable.ic_star_selected,
        unselectedIcon = R.drawable.ic_star_unselected,
        testTagKey = BottomBarTestTags.FAVORITES,
    )

    data object LeaderboardScreen : NavigationItem(
        route = Leaderboard,
        title = R.string.leaderboard,
        selectedIcon = R.drawable.ic_leaderboard_selected,
        unselectedIcon = R.drawable.ic_leaderboard_unselected,
        testTagKey = BottomBarTestTags.LEADERBOARD,
    )

    data object ProfileScreen : NavigationItem(
        route = Profile,
        title = R.string.profile,
        selectedIcon = R.drawable.ic_profile_selected,
        unselectedIcon = R.drawable.ic_profile_unselected,
        testTagKey = BottomBarTestTags.PROFILE,
    )

    companion object {
        fun getNavigationItems() = listOf(HomeScreen, FavoritesScreen, LeaderboardScreen, ProfileScreen)
    }
}