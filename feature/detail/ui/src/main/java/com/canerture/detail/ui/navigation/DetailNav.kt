package com.canerture.detail.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.canerture.detail.ui.DetailScreen
import com.canerture.detail.ui.DetailViewModel
import com.canerture.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
data object Detail : Screen

fun NavGraphBuilder.detailScreen(

) {
    composable<Detail> {
        val viewModel = hiltViewModel<DetailViewModel>()
        val uiEffect = viewModel.uiEffect
        DetailScreen(
            uiEffect = uiEffect,
        )
    }
}