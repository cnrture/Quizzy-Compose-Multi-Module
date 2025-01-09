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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.collectWithLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    result: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(this, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state) {
            this@collectWithLifecycle.collect { effect ->
                result(effect)
            }
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

fun Modifier.boldBorder(radius: Int = 16): Modifier = composed {
    this.border(
        width = 2.dp,
        color = QuizAppTheme.colors.black,
        shape = RoundedCornerShape(radius.dp)
    )
}