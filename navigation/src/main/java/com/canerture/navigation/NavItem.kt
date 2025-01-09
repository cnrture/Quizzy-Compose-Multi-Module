package com.canerture.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.canerture.ui.navigation.Screen

data class NavItem(
    val screen: Screen,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)