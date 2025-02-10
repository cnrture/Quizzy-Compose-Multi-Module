package com.canerture.ui.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow

val <T> Flow<T>.collectWithLifecycle: @Composable (result: (T) -> Unit) -> Unit
    @Composable get() = { function ->
        CollectWithLaunchedEffect {
            function(it)
        }
    }

@Composable
fun <T> Flow<T>.CollectWithLaunchedEffect(
    result: (T) -> Unit,
) {
    LaunchedEffect(Unit) {
        collect { effect ->
            result(effect)
        }
    }
}

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

@Composable
fun Modifier.boldBorder(
    radius: Int = 16,
    color: Color = QuizAppTheme.colors.onBackground.copy(alpha = 0.8f),
    width: Dp = 2.dp
): Modifier {
    return this.border(
        width = width,
        color = color,
        shape = RoundedCornerShape(radius.dp)
    )
}

inline fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier {
    return if (condition) {
        this then modifier(Modifier)
    } else {
        this
    }
}