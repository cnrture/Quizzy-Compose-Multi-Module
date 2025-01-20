package com.canerture.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun QuizAppText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = QuizAppTheme.colors.black,
    style: TextStyle = QuizAppTheme.typography.paragraph2,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = style,
        overflow = overflow,
        maxLines = maxLines
    )
}

@Composable
fun QuizAppText(
    modifier: Modifier = Modifier,
    fullText: String,
    spanTexts: List<String>,
    color: Color = QuizAppTheme.colors.black,
    style: TextStyle = QuizAppTheme.typography.paragraph2,
    textAlign: TextAlign? = null,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = style.toSpanStyle()) {
                append(fullText)
                spanTexts.forEach {
                    val mStartIndex = fullText.indexOf(it)
                    val mEndIndex = mStartIndex.plus(it.length)
                    addStyle(
                        style = SpanStyle(
                            color = QuizAppTheme.colors.blue,
                            fontWeight = FontWeight.Bold,
                        ),
                        start = mStartIndex,
                        end = mEndIndex,
                    )
                }
            }
        },
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = style
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizAppTextPreview() {
    QuizAppText(
        text = "QuizAppText"
    )
}