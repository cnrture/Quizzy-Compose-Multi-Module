package com.canerture.navigation

import com.canerture.favorites.ui.navigation.Favorites
import com.canerture.home.ui.navigation.Home
import com.canerture.leaderboard.ui.navigation.Leaderboard
import com.canerture.profile.ui.navigation.Profile
import com.canerture.quiz.navigation.R
import com.canerture.ui.navigation.Screen

sealed class NavigationItem(
    var route: Screen,
    var title: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int,
) {
    data object HomeScreen : NavigationItem(
        route = Home,
        title = R.string.home,
        selectedIcon = R.drawable.ic_home_selected,
        unselectedIcon = R.drawable.ic_home_unselected,
    )

    data object FavoritesScreen : NavigationItem(
        route = Favorites,
        title = R.string.favorites,
        selectedIcon = R.drawable.ic_star_selected,
        unselectedIcon = R.drawable.ic_star_unselected,
    )

    data object LeaderboardScreen : NavigationItem(
        route = Leaderboard,
        title = R.string.leaderboard,
        selectedIcon = R.drawable.ic_leaderboard_selected,
        unselectedIcon = R.drawable.ic_leaderboard_unselected,
    )

    data object ProfileScreen : NavigationItem(
        route = Profile,
        title = R.string.profile,
        selectedIcon = R.drawable.ic_profile_selected,
        unselectedIcon = R.drawable.ic_profile_unselected,
    )

    companion object {
        fun getNavigationRoutes() = listOf(
            HomeScreen.route.getRoute(),
            FavoritesScreen.route.getRoute(),
            LeaderboardScreen.route.getRoute(),
            ProfileScreen.route.getRoute(),
        )

        fun getNavigationItems() = listOf(HomeScreen, FavoritesScreen, LeaderboardScreen, ProfileScreen)
    }
}