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

    val database: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_database)

    val discoverSelected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_discover_selected)

    val discoverUnselected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_discover_unselected)

    val filter: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_filter)

    val hint: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_hint)

    val homeSelected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_home_selected)

    val homeUnselected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_home_unselected)

    val leaderboardSelected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_leaderboard_selected)

    val leaderboardUnselected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_leaderboard_unselected)

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

    val profileSelected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_profile_selected)

    val profileUnselected: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_profile_unselected)

    val search: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_search)

    val settings: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_settings)

    val share: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_share)

    val sort: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_sort)

    val star: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_star)

    val timer: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_timer)

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
}