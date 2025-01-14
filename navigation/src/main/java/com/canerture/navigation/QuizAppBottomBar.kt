package com.canerture.navigation

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.conditional
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@SuppressLint("RestrictedApi")
@Composable
fun QuizAppBottomBar(
    navController: NavController,
) {
    val tabList = NavigationItem.getNavigationItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = QuizAppTheme.colors.white,
    ) {
        tabList.forEach { navItem ->
            val isSelected = currentRoute == navItem.route.getRoute()
            key(navItem.route.getRoute()) {
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
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
                        imageVector = ImageVector.vectorResource(if (isSelected) navItem.selectedIcon else navItem.unselectedIcon),
                        tint = if (isSelected) QuizAppTheme.colors.white else QuizAppTheme.colors.black,
                        contentDescription = null,
                    )
                    AnimatedVisibility(isSelected) {
                        QuizAppText(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(navItem.title),
                            style = QuizAppTheme.typography.paragraph2,
                            color = QuizAppTheme.colors.white,
                        )
                    }
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
    )
}