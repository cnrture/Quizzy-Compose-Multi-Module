package com.canerture.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = QuizAppTheme.colors.black,
                shape = RoundedCornerShape(16.dp),
            )
            .background(QuizAppTheme.colors.white),
        value = value,
        onValueChange = onValueChange,
        textStyle = QuizAppTheme.typography.paragraph1,
        singleLine = true,
        decorationBox = {
            Box(
                modifier = Modifier.padding(16.dp),
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
                            text = "Search...",
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