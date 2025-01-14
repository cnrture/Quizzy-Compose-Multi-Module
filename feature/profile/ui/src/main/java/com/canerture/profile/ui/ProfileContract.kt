package com.canerture.profile.ui

import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel

object ProfileContract {
    data class UiState(
        val isLoading: Boolean = false,
        val profile: ProfileModel? = null,
        val rank: RankModel? = null,
    )

    sealed interface UiAction {
        data object OnEditProfileClick : UiAction
    }

    sealed interface UiEffect {
        data object NavigateEditProfile : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}