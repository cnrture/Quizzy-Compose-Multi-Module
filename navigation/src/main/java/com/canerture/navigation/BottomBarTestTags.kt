package com.canerture.navigation

internal object BottomBarTestTags {
    const val TAB_LABEL_PREFIX = "bottomBar.tab"

    fun tabLabel(item: NavigationItem): String = "$TAB_LABEL_PREFIX.${item::class.simpleName}"
}
