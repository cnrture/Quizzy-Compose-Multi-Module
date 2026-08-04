package com.canerture.profile.ui

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.canerture.feature.profile.ui.R
import com.canerture.profile.ui.ProfileContract.UiAction
import com.canerture.profile.ui.ProfileContract.UiEffect
import com.canerture.profile.ui.ProfileContract.UiState
import com.canerture.profile.ui.component.RankItem
import com.canerture.ui.components.QuizzyAsyncImage
import com.canerture.ui.components.QuizzyButton
import com.canerture.ui.components.QuizzyButtonSize
import com.canerture.ui.components.QuizzyButtonType
import com.canerture.ui.components.QuizzyLoading
import com.canerture.ui.components.QuizzyText
import com.canerture.ui.components.QuizzyToolbar
import com.canerture.ui.components.QuizzyScaffold
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
    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateEditProfile -> onNavigateEditProfile()
            is UiEffect.Logout -> onLogout()
            is UiEffect.ShowError -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    QuizzyScaffold(
        topBar = {
            QuizzyToolbar(
                title = stringResource(R.string.profile_title),
                endIcon = QuizAppTheme.icons.exit,
                onEndIconClick = { onAction(UiAction.OnLogoutClick) },
            )
        },
    ) { paddingValues ->
        ProfileContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 32.dp, end = 32.dp, top = 16.dp),
            uiState = uiState,
            onEditProfileClick = { onAction(UiAction.OnEditProfileClick) },
        )
    }

    if (uiState.isLoading) QuizzyLoading()
}

@Composable
internal fun ProfileContent(
    uiState: UiState,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizzyAsyncImage(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color = QuizAppTheme.colors.background,
                    shape = CircleShape,
                )
                .boldBorder(100)
                .padding(24.dp),
            imageUrl = uiState.profile?.avatarUrl.orEmpty(),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.height(32.dp))
        QuizzyText(
            text = stringResource(R.string.nickname, uiState.profile?.username.orEmpty()),
            style = QuizAppTheme.typography.heading3,
            color = QuizAppTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuizzyButton(
            text = stringResource(R.string.edit_profile),
            type = QuizzyButtonType.SECONDARY,
            size = QuizzyButtonSize.SMALL,
            onClick = onEditProfileClick,
        )
        Spacer(modifier = Modifier.height(48.dp))
        QuizzyText(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.your_rank),
            style = QuizAppTheme.typography.heading3,
            color = QuizAppTheme.colors.onBackground,
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

@PreviewLightDark
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