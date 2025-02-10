package com.canerture.quizappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.canerture.navigation.LoginFlow
import com.canerture.navigation.MainFlow
import com.canerture.navigation.NavigationItem
import com.canerture.navigation.QuizAppBottomBar
import com.canerture.navigation.QuizAppNavGraph
import com.canerture.navigation.navigateWithPopUpTo
import com.canerture.ui.components.QuizAppDialog
import com.canerture.ui.extensions.collectWithLifecycle
import com.canerture.ui.theme.QuizAppTheme
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

            val visibleBottomSheetScreen = NavigationItem.getNavigationRoutes()

            val bottomBarVisibility =
                navController.currentBackStackEntryAsState().value?.destination?.route in visibleBottomSheetScreen

            QuizAppTheme {
                Box {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = QuizAppTheme.colors.background,
                        content = { innerPadding ->
                            QuizAppNavGraph(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                navController = navController,
                            )
                        },
                        bottomBar = {
                            AnimatedVisibility(bottomBarVisibility) {
                                Column {
                                    HorizontalDivider(
                                        thickness = 2.dp,
                                        color = QuizAppTheme.colors.onBackground,
                                    )
                                    QuizAppBottomBar(
                                        navController = navController,
                                    )
                                }
                            }
                        }
                    )
                }
                if (uiState.isShowNoNetworkDialog) {
                    QuizAppDialog(
                        isSuccess = false,
                        isCancelable = false,
                        message = stringResource(R.string.no_network_connection),
                        onButtonClick = {
                            viewModel.updateUiState { copy(isShowNoNetworkDialog = false) }
                        }
                    )
                }
            }
        }
    }
}