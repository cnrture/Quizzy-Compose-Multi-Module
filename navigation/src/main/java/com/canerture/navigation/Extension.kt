package com.canerture.navigation

import androidx.navigation.NavHostController
import com.canerture.ui.navigation.Screen

fun NavHostController.navigateWithPopUpTo(screen: Any, popUp: Any) {
    navigate(screen) {
        popUpTo(popUp) { inclusive = true }
    }
}

fun Screen.getRoute(): String {
    return this::class.java.canonicalName.orEmpty()
}