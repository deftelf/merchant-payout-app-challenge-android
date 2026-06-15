package com.example.androidinterview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.androidinterview.ui.HomeDestination
import com.example.androidinterview.ui.RecentActivityDestination
import com.example.androidinterview.ui.activity.RecentActivityScreen
import com.example.androidinterview.ui.home.HomeScreen
import com.example.androidinterview.ui.payout.PayoutScreen
import com.example.androidinterview.ui.theme.AndroidInterviewTheme
import dagger.hilt.android.AndroidEntryPoint

private enum class Tab { Home, Payout }

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
                            var selectedTab by rememberSaveable { mutableStateOf(Tab.Home) }
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = {
                                    val selectedItemColors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF1976D2),
                                        selectedTextColor = Color(0xFF1976D2),
                                        indicatorColor = Color.Transparent,
                                    )
                                    NavigationBar(
                                        containerColor = Color.White,
                                    ) {
                                        NavigationBarItem(
                                            selected = selectedTab == Tab.Home,
                                            onClick = { selectedTab = Tab.Home },
                                            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                            label = { Text("Home") },
                                            colors = selectedItemColors,
                                        )
                                        NavigationBarItem(
                                            selected = selectedTab == Tab.Payout,
                                            onClick = { selectedTab = Tab.Payout },
                                            icon = { Icon(Icons.Filled.ArrowCircleUp, contentDescription = "Payout") },
                                            label = { Text("Payout") },
                                            colors = selectedItemColors,
                                        )
                                    }
                                },
                            ) { padding ->
                                when (selectedTab) {
                                    Tab.Home -> HomeScreen(
                                        modifier = Modifier.padding(padding),
                                        onShowMore = { merchant ->
                                            backStack.add(RecentActivityDestination(merchant))
                                        },
                                    )
                                    Tab.Payout -> PayoutScreen(modifier = Modifier.padding(padding))
                                }
                            }
                        }
                        entry<RecentActivityDestination> { key ->
                            RecentActivityScreen(
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
