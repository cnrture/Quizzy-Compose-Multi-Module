package com.canerture.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalLightColors = staticCompositionLocalOf { lightColors() }
internal val LocalDarkColors = staticCompositionLocalOf { darkColors() }

internal fun darkColors(
    onBackground: Color = Color(0xFFfffbf3),
    background: Color = Color(0xFF111111),
    blue: Color = Color(0xFF609DED),
    lightBlue: Color = Color(0xFFBCD9FF).copy(alpha = 0.5f),
    yellow: Color = Color(0xFFFFCB46),
    lightYellow: Color = Color(0xFFFFECBC).copy(alpha = 0.5f),
    green: Color = Color(0xFF2ED22A),
    softGreen: Color = Color(0xFFC2F8B9).copy(alpha = 0.5f),
    red: Color = Color(0xFFF45C5C),
    softRed: Color = Color(0xFFFFD0BC).copy(alpha = 0.5f),
): QuizAppColor = QuizAppColor(
    background = background,
    onBackground = onBackground,
    blue = blue,
    lightBlue = lightBlue,
    yellow = yellow,
    lightYellow = lightYellow,
    green = green,
    softGreen = softGreen,
    red = red,
    softRed = softRed,
)

internal fun lightColors(
    background: Color = Color(0xFFfffbf3),
    onBackground: Color = Color(0xFF111111),
    blue: Color = Color(0xFF609DED),
    lightBlue: Color = Color(0xFFBCD9FF),
    yellow: Color = Color(0xFFFFCB46),
    lightYellow: Color = Color(0xFFFFECBC),
    green: Color = Color(0xFF2ED22A),
    softGreen: Color = Color(0xFFC2F8B9),
    red: Color = Color(0xFFF45C5C),
    softRed: Color = Color(0xFFFFD0BC),
): QuizAppColor = QuizAppColor(
    background = background,
    onBackground = onBackground,
    blue = blue,
    lightBlue = lightBlue,
    yellow = yellow,
    lightYellow = lightYellow,
    green = green,
    softGreen = softGreen,
    red = red,
    softRed = softRed,
)

data class QuizAppColor(
    val background: Color,
    val onBackground: Color,
    val blue: Color,
    val lightBlue: Color,
    val yellow: Color,
    val lightYellow: Color,
    val green: Color,
    val softGreen: Color,
    val red: Color,
    val softRed: Color,
)