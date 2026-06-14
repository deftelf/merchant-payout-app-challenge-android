package com.example.androidinterview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.androidinterview.ui.ActivityDestination
import com.example.androidinterview.ui.HomeDestination
import com.example.androidinterview.ui.activity.ActivityScreen
import com.example.androidinterview.ui.home.HomeScreen
import com.example.androidinterview.ui.theme.AndroidInterviewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(HomeDestination)

            AndroidInterviewTheme {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider {
                        entry<HomeDestination> {
                            Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                                HomeScreen(
                                    modifier = Modifier.padding(padding),
                                    onShowMore = { merchant ->
                                        backStack.add(ActivityDestination(merchant))
                                    },
                                )
                            }
                        }
                        entry<ActivityDestination> { key ->
                            ActivityScreen(
                                merchant = key.merchant,
                                onBack = { backStack.removeLastOrNull() },
                            )
                        }
                    },
                )
            }
        }
    }
}
