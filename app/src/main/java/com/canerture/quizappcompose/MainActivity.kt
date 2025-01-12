package com.canerture.quizappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.canerture.navigation.LoginFlow
import com.canerture.navigation.MainFlow
import com.canerture.navigation.QuizAppNavGraph
import com.canerture.navigation.navigateWithPopUpTo
import com.canerture.ui.components.QuizAppDialog
import com.canerture.ui.extensions.collectWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            viewModel.uiEffect.collectWithLifecycle {
                when (it) {
                    is MainContract.UiEffect.NavigateLogin -> {
                        navController.navigateWithPopUpTo(LoginFlow, MainFlow)
                    }
                }
            }

            Box {
                QuizAppNavGraph(navController)

                if (uiState.isShowNoNetworkDialog) {
                    QuizAppDialog(
                        isSuccess = false,
                        isCancelable = false,
                        message = "No network connection",
                        buttonText = "Okay",
                        onButtonClick = {
                            viewModel.updateUiState { copy(isShowNoNetworkDialog = false) }
                        }
                    )
                }
            }
        }
    }
}