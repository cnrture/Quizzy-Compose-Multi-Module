package com.canerture.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.home.ui.R
import com.canerture.home.domain.model.CategoryModel
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun CategoryItem(
    category: CategoryModel,
    index: Int,
    isLastItem: Boolean,
    onCategoryClick: (CategoryModel) -> Unit,
) {
    if (index == 0) Spacer(modifier = Modifier.width(32.dp))
    val bgColor = if (index % 2 == 0) QuizAppTheme.colors.lightBlue else QuizAppTheme.colors.lightYellow
    Column(
        modifier = Modifier
            .width(160.dp)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(16.dp),
            )
            .boldBorder()
            .clip(RoundedCornerShape(16.dp))
            .noRippleClickable { onCategoryClick(category) }
            .padding(16.dp),
    ) {
        QuizAppAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(124.dp)
                .boldBorder(16)
                .clip(RoundedCornerShape(16.dp)),
            imageUrl = category.imageUrl,
            contentDescription = category.name,
        )
        Spacer(modifier = Modifier.height(16.dp))
        QuizAppText(
            text = category.name,
            style = QuizAppTheme.typography.heading5,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(12.dp))
        QuizAppText(
            text = stringResource(R.string.quiz_count, category.quizCount),
            style = QuizAppTheme.typography.heading6,
            color = QuizAppTheme.colors.black,
        )
    }
    Spacer(modifier = Modifier.width(if (isLastItem) 32.dp else 16.dp))
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    CategoryItem(
        category = CategoryModel(
            id = 1,
            name = "Category 1",
            imageUrl = "https://www.canerture.com/images/category1.jpg",
            quizCount = 5,
        ),
        index = 0,
        isLastItem = false,
        onCategoryClick = {},
    )
}