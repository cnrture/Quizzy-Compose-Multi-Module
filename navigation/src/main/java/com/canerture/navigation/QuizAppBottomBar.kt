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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    val tabList = NavigationItem.getNavigationItems()

    NavigationBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = QuizAppTheme.colors.white,
    ) {
        tabList.forEachIndexed { index, navItem ->
            val isSelected = selectedItem == index
            val weight = if (isSelected) 1f else 0.5f
            val bgColor = if (isSelected) QuizAppTheme.colors.blue else QuizAppTheme.colors.white
            val horizontalPadding = if (isSelected) 20.dp else 0.dp
            val iconSize = if (isSelected) 16.dp else 24.dp
            val icon = if (isSelected) navItem.selectedIcon else navItem.unselectedIcon
            val tint = if (isSelected) QuizAppTheme.colors.white else QuizAppTheme.colors.black
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(weight)
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .conditional(isSelected) { boldBorder() }
                    .noRippleClickable {
                        navController.navigate(navItem.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .padding(
                        horizontal = horizontalPadding,
                        vertical = 12.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = ImageVector.vectorResource(icon),
                    tint = tint,
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

@Preview(showBackground = true)
@Composable
fun QuizAppBottomBarPreview() {
    QuizAppBottomBar(
        navController = rememberNavController(),
        selectedItem = 0,
    )
}