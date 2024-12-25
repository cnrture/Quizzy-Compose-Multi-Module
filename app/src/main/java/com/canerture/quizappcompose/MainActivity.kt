package com.canerture.quizappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.canerture.navigation.QuizAppNavGraph
import com.canerture.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = QuizAppTheme.colors.background,
                    content = { innerPadding ->
                        val navController = rememberNavController()
                        QuizAppNavGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            navController = navController,
                        )
                    }
                )
            }
        }
    }
}