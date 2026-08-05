package com.canerture.quizappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.canerture.navigation.LoginFlow
import com.canerture.navigation.MainFlow
import com.canerture.navigation.NavigationItem
import com.canerture.navigation.QuizAppBottomBar
import com.canerture.navigation.QuizAppNavGraph
import com.canerture.navigation.navigateWithPopUpTo
import com.canerture.ui.components.QuizzyDialog
import com.canerture.ui.components.QuizzyScaffold
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

            val currentDestination = navController.currentBackStackEntryAsState().value?.destination

            val isBottomBarVisible = NavigationItem.getNavigationItems().any { item ->
                currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
            }

            QuizAppTheme {
                QuizzyScaffold(
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
                        AnimatedVisibility(isBottomBarVisible) {
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
                if (uiState.isShowNoNetworkDialog) {
                    QuizzyDialog(
                        testTag = MainTestTags.NO_NETWORK_DIALOG,
                        isSuccess = false,
                        isCancelable = false,
                        message = stringResource(R.string.no_network_connection),
                        onButtonClick = {
                            viewModel.onAction(MainContract.UiAction.DismissNoNetworkDialog)
                        }
                    )
                }
            }
        }
    }
}