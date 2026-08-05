package com.canerture.editprofile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.canerture.editprofile.domain.model.AvatarModel
import com.canerture.editprofile.ui.EditProfileTestTags
import com.canerture.feature.editprofile.ui.R
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzyButton
import com.canerture.ui.components.QuizzyButtonSize
import com.canerture.ui.components.QuizzySpacer
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.noRippleClickable
import com.canerture.ui.theme.QuizAppTheme

@Composable
internal fun AvatarsDialog(
    avatars: List<AvatarModel>,
    onSelectAvatar: (AvatarModel) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .semantics { testTagsAsResourceId = true }
                .fillMaxSize()
                .noRippleClickable { onDismiss() }
                .padding(horizontal = 48.dp, vertical = 200.dp)
                .background(
                    color = QuizAppTheme.colors.background,
                    shape = RoundedCornerShape(16.dp)
                )
                .boldBorder(width = 4.dp, color = QuizAppTheme.colors.blue)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuizzyText(
                testTag = EditProfileTestTags.AVATARS_DIALOG_TITLE,
                text = stringResource(R.string.select_avatar),
                style = QuizAppTheme.typography.heading3,
            )
            QuizzySpacer(16.dp)
            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(3),
            ) {
                items(avatars.size) { index ->
                    QuizzyAsyncImage(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .background(
                                color = QuizAppTheme.colors.background,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .boldBorder(100)
                            .clickable { onSelectAvatar(avatars[index]) }
                            .padding(16.dp),
                        imageUrl = avatars[index].url,
                        testTag = "${EditProfileTestTags.AVATARS_DIALOG_AVATAR_ITEM}$index",
                        contentDescription = stringResource(R.string.profile_image),
                    )
                }
            }
            QuizzySpacer(16.dp)
            QuizzyButton(
                modifier = Modifier.fillMaxWidth(),
                testTag = EditProfileTestTags.AVATARS_DIALOG_CLOSE_BUTTON,
                size = QuizzyButtonSize.SMALL,
                text = stringResource(R.string.close),
                onClick = onDismiss,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun AvatarsDialogPreview() {
    AvatarsDialog(
        avatars = listOf(
            AvatarModel(1, "https://www.canerture.com/avatar1.jpg"),
            AvatarModel(2, "https://www.canerture.com/avatar2.jpg"),
            AvatarModel(3, "https://www.canerture.com/avatar3.jpg"),
            AvatarModel(4, "https://www.canerture.com/avatar4.jpg"),
            AvatarModel(5, "https://www.canerture.com/avatar5.jpg"),
            AvatarModel(6, "https://www.canerture.com/avatar6.jpg"),
        ),
        onSelectAvatar = {},
        onDismiss = {},
    )
}