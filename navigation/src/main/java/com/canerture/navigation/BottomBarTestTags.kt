package com.canerture.navigation

internal object BottomBarTestTags {
    const val TAB_LABEL_PREFIX = "bottomBar.tab"

    const val HOME = "HomeScreen"
    const val FAVORITES = "FavoritesScreen"
    const val LEADERBOARD = "LeaderboardScreen"
    const val PROFILE = "ProfileScreen"

    fun tabLabel(item: NavigationItem): String = "$TAB_LABEL_PREFIX.${item.testTagKey}"
}
