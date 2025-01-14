package com.canerture.profile.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel

class ProfilePreviewProvider : PreviewParameterProvider<ProfileContract.UiState> {
    override val values: Sequence<ProfileContract.UiState>
        get() = sequenceOf(
            ProfileContract.UiState(
                isLoading = false,
                profile = ProfileModel(
                    email = "",
                    username = "canerture",
                    avatarUrl = "",
                ),
                rank = RankModel(
                    rank = "1",
                    score = "100",
                ),
            ),
            ProfileContract.UiState(
                isLoading = true,
            ),
        )
}