package com.canerture.favorites.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canerture.feature.favorites.ui.R
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
        QuizAppTheme.colors.red
    } else {
        Color.Transparent
    }
    if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = color,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = QuizAppTheme.icons.delete,
                tint = QuizAppTheme.colors.background,
                contentDescription = stringResource(R.string.delete),
            )
        }
    }
}