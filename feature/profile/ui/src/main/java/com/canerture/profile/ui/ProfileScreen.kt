package com.canerture.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.profile.ui.R
import com.canerture.profile.ui.ProfileContract.UiAction
import com.canerture.profile.ui.ProfileContract.UiEffect
import com.canerture.profile.ui.ProfileContract.UiState
import com.canerture.profile.ui.component.RankItem
import com.canerture.ui.components.QuizAppAsyncImage
import com.canerture.ui.components.QuizAppButton
import com.canerture.ui.components.QuizAppButtonSize
import com.canerture.ui.components.QuizAppButtonType
import com.canerture.ui.components.QuizAppLoading
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.components.QuizAppToolbar
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun ProfileScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateEditProfile -> onNavigateEditProfile()
            is UiEffect.Logout -> onLogout()
            is UiEffect.ShowError -> {
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizAppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppToolbar(
            title = stringResource(R.string.profile_title),
            endIcon = QuizAppTheme.icons.exit,
            onEndIconClick = { onAction(UiAction.OnLogoutClick) },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileContent(
            uiState = uiState,
            onEditProfileClick = { onAction(UiAction.OnEditProfileClick) },
        )
    }

    if (uiState.isLoading) QuizAppLoading()
}

@Composable
internal fun ProfileContent(
    uiState: UiState,
    onEditProfileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizAppAsyncImage(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color = QuizAppTheme.colors.white,
                    shape = CircleShape,
                )
                .boldBorder(100)
                .padding(24.dp),
            imageUrl = uiState.profile?.avatarUrl.orEmpty(),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.height(32.dp))
        QuizAppText(
            text = stringResource(R.string.nickname, uiState.profile?.username.orEmpty()),
            style = QuizAppTheme.typography.heading3,
            color = QuizAppTheme.colors.black,
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizAppButton(
            text = stringResource(R.string.edit_profile),
            type = QuizAppButtonType.SECONDARY,
            size = QuizAppButtonSize.SMALL,
            onClick = onEditProfileClick,
        )
        Spacer(modifier = Modifier.height(48.dp))
        QuizAppText(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.your_rank),
            style = QuizAppTheme.typography.heading3,
            color = QuizAppTheme.colors.black,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.rank != null) {
            RankItem(
                rank = uiState.rank,
                username = uiState.profile?.username.orEmpty(),
                avatarUrl = uiState.profile?.avatarUrl.orEmpty(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun ProfileScreenPreview(
    @PreviewParameter(ProfilePreviewProvider::class) uiState: UiState,
) {
    ProfileScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateEditProfile = {},
        onLogout = {},
    )
}