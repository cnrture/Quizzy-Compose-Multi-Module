package com.canerture.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.canerture.core.ui.R

internal val LocalIcons = staticCompositionLocalOf { QuizAppIcons() }

class QuizAppIcons {
    val arrowLeft: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_arrow_left)

    val check: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_check)

    val close: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_close)

    val lock: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_lock)

    val email: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_mail)

    val more: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_more)

    val notifications: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_notifications)

    val profileUnselected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_profile)

    val search: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_search)

    val settings: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_settings)

    val sort: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_sort)

    val starSelected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_star_selected)

    val starUnselected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_star_unselected)

    val trophy: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_trophy)

    val visibilityOn: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_visibility_on)

    val visibilityOff: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_visibility_off)

    val wrong: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_wrong)

    val starPattern: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_star_pattern)

    val google: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_google)

    val logo: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_logo)

    val happy: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_happy)

    val sad: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_sad)

    val smile: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_smile)

    val king: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_king)
}