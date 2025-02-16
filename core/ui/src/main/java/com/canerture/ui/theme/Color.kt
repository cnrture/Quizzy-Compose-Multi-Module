package com.canerture.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class QuizAppColor(
    background: Color,
    onBackground: Color,
    blue: Color,
    lightBlue: Color,
    yellow: Color,
    lightYellow: Color,
    green: Color,
    softGreen: Color,
    red: Color,
    softRed: Color,
) {
    private var _background: Color by mutableStateOf(background)
    val background: Color = _background

    private var _onBackground: Color by mutableStateOf(onBackground)
    val onBackground: Color = _onBackground

    private var _blue: Color by mutableStateOf(blue)
    val blue: Color = _blue

    private var _lightBlue: Color by mutableStateOf(lightBlue)
    val lightBlue: Color = _lightBlue

    private var _yellow: Color by mutableStateOf(yellow)
    val yellow: Color = _yellow

    private var _lightYellow: Color by mutableStateOf(lightYellow)
    val lightYellow: Color = _lightYellow

    private var _green: Color by mutableStateOf(green)
    val green: Color = _green

    private var _softGreen: Color by mutableStateOf(softGreen)
    val softGreen: Color = _softGreen

    private var _red: Color by mutableStateOf(red)
    val red: Color = _red

    private var _softRed: Color by mutableStateOf(softRed)
    val softRed: Color = _softRed
}

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