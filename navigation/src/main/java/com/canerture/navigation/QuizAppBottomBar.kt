package com.canerture.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.canerture.favorites.ui.navigation.Favorites
import com.canerture.home.ui.navigation.Home
import com.canerture.leaderboard.ui.navigation.Leaderboard
import com.canerture.profile.ui.navigation.Profile
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.conditional
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppBottomBar(
    navController: NavController,
    selectedItem: Int,
) {
    val tabList = listOf(
        NavItem(
            screen = Home,
            title = "Home",
            selectedIcon = QuizAppTheme.icons.homeSelected,
            unselectedIcon = QuizAppTheme.icons.homeUnselected,
        ),
        NavItem(
            screen = Favorites,
            title = "Favorites",
            selectedIcon = QuizAppTheme.icons.starSelected,
            unselectedIcon = QuizAppTheme.icons.starUnselected,
        ),
        NavItem(
            screen = Leaderboard,
            title = "Stats",
            selectedIcon = QuizAppTheme.icons.leaderboardSelected,
            unselectedIcon = QuizAppTheme.icons.leaderboardUnselected,
        ),
        NavItem(
            screen = Profile,
            title = "Profile",
            selectedIcon = QuizAppTheme.icons.profileSelected,
            unselectedIcon = QuizAppTheme.icons.profileUnselected,
        ),
    )

    NavigationBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = QuizAppTheme.colors.white,
    ) {
        tabList.forEachIndexed { index, navItem ->
            val isSelected = selectedItem == index
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(if (isSelected) 1f else 0.5f)
                    .background(
                        color = if (isSelected) QuizAppTheme.colors.blue else QuizAppTheme.colors.white,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .conditional(isSelected) { boldBorder() }
                    .noRippleClickable {
                        navController.navigate(navItem.screen) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .padding(
                        horizontal = if (isSelected) 20.dp else 0.dp,
                        vertical = 12.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(if (isSelected) 16.dp else 24.dp),
                    imageVector = if (isSelected) navItem.selectedIcon else navItem.unselectedIcon,
                    tint = if (isSelected) QuizAppTheme.colors.white else QuizAppTheme.colors.black,
                    contentDescription = null,
                )
                AnimatedVisibility(isSelected) {
                    QuizAppText(
                        modifier = Modifier.padding(start = 8.dp),
                        text = navItem.title,
                        style = QuizAppTheme.typography.paragraph2,
                        color = QuizAppTheme.colors.white,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppBottomBarPreview() {
    QuizAppBottomBar(
        navController = rememberNavController(),
        2,
    )
}