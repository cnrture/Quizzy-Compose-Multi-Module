package com.canerture.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.core.ui.R
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.conditional
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppSearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .boldBorder()
            .background(QuizAppTheme.colors.white),
        value = value,
        onValueChange = onValueChange,
        textStyle = QuizAppTheme.typography.paragraph1,
        singleLine = true,
        decorationBox = {
            Box(
                modifier = Modifier
                    .conditional(onClick != null) {
                        noRippleClickable { onClick?.invoke() }
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        imageVector = QuizAppTheme.icons.search,
                        tint = QuizAppTheme.colors.black,
                        contentDescription = null,
                    )
                    if (value.isBlank()) {
                        QuizAppText(
                            text = stringResource(R.string.search),
                            style = QuizAppTheme.typography.paragraph1,
                            color = QuizAppTheme.colors.darkGray
                        )
                    } else {
                        it()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizAppSearchBarPreview() {
    QuizAppSearchBar(
        value = "QuizAppSearchBar",
        onValueChange = {},
    )
}