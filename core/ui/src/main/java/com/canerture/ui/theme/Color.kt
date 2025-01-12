package com.canerture.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalColors = staticCompositionLocalOf { colors() }

fun colors(
    black: Color = Color(0xFF343434),
    white: Color = Color(0xFFFCFCFC),
    background: Color = Color(0xFFfffbf3),
    blue: Color = Color(0xFF609DED),
    lightBlue: Color = Color(0xFFBCD9FF),
    yellow: Color = Color(0xFFFFCB46),
    lightYellow: Color = Color(0xFFFFECBC),
    darkGray: Color = Color(0xFFB9B9B9),
    gray: Color = Color(0xFF9C9C9C).copy(alpha = 0.5f),
    softGray: Color = Color(0xFFF2F2F2),
    green: Color = Color(0xFF2ED22A),
    softGreen: Color = Color(0xFFC2F8B9),
    red: Color = Color(0xFFF45C5C),
    softRed: Color = Color(0xFFFFD0BC),
): QuizAppColor = QuizAppColor(
    black = black,
    white = white,
    background = background,
    blue = blue,
    lightBlue = lightBlue,
    yellow = yellow,
    lightYellow = lightYellow,
    darkGray = darkGray,
    gray = gray,
    softGray = softGray,
    green = green,
    softGreen = softGreen,
    red = red,
    softRed = softRed,
)

class QuizAppColor(
    black: Color,
    white: Color,
    background: Color,
    blue: Color,
    lightBlue: Color,
    yellow: Color,
    lightYellow: Color,
    darkGray: Color,
    gray: Color,
    softGray: Color,
    green: Color,
    softGreen: Color,
    red: Color,
    softRed: Color,
) {
    private var _black: Color by mutableStateOf(black)
    val black: Color = _black

    private var _white: Color by mutableStateOf(white)
    val white: Color = _white

    private var _background: Color by mutableStateOf(background)
    val background: Color = _background

    private var _blue: Color by mutableStateOf(blue)
    val blue: Color = _blue

    private var _lightBlue: Color by mutableStateOf(lightBlue)
    val lightBlue: Color = _lightBlue

    private var _yellow: Color by mutableStateOf(yellow)
    val yellow: Color = _yellow

    private var _lightYellow: Color by mutableStateOf(lightYellow)
    val lightYellow: Color = _lightYellow

    private var _darkGray: Color by mutableStateOf(darkGray)
    val darkGray: Color = _darkGray

    private var _gray: Color by mutableStateOf(gray)
    val gray: Color = _gray

    private var _softGray: Color by mutableStateOf(softGray)
    val softGray: Color = _softGray

    private var _green: Color by mutableStateOf(green)
    val green: Color = _green

    private var _softGreen: Color by mutableStateOf(softGreen)
    val softGreen: Color = _softGreen

    private var _red: Color by mutableStateOf(red)
    val red: Color = _red

    private var _softRed: Color by mutableStateOf(softRed)
    val softRed: Color = _softRed
}