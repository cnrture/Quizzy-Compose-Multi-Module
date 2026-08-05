package com.canerture.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.canerture.feature.home.ui.R
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.ui.HomeTestTags
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun CategoryItem(
    category: CategoryModel,
    index: Int,
    isLastItem: Boolean,
    onCategoryClick: (CategoryModel) -> Unit,
) {
    if (index == 0) QuizzySpacer(32.dp)
    val bgColor =
        if (index % 2 == 0) QuizAppTheme.colors.lightBlue else QuizAppTheme.colors.lightYellow
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
        QuizzyAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(124.dp)
                .boldBorder(16)
                .clip(RoundedCornerShape(16.dp)),
            testTag = HomeTestTags.CATEGORY_ITEM_IMAGE,
            imageUrl = category.imageUrl,
            contentDescription = category.name,
        )
        QuizzySpacer(16.dp)
        QuizzyText(
            testTag = HomeTestTags.CATEGORY_ITEM_NAME_TEXT,
            text = category.name,
            style = QuizAppTheme.typography.heading5,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        QuizzySpacer(12.dp)
        QuizzyText(
            testTag = HomeTestTags.CATEGORY_ITEM_QUIZ_COUNT_TEXT,
            text = stringResource(R.string.quiz_count, category.quizCount),
            style = QuizAppTheme.typography.heading6,
            color = QuizAppTheme.colors.onBackground.copy(alpha = 0.5f),
        )
    }
    QuizzySpacer(if (isLastItem) 32.dp else 16.dp)
}

@PreviewLightDark
@Composable
internal fun CategoryItemPreview() {
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