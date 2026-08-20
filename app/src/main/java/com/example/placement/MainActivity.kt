package com.example.placement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.placement.ui.screens.ChartsScreen
import com.example.placement.ui.screens.DatasetExplorerScreen
import com.example.placement.ui.screens.PredictorScreen
import com.example.placement.ui.screens.WhatIfScreen
import com.example.placement.ui.theme.PlacementPredictorTheme
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary
import com.example.placement.viewmodel.AppTab
import com.example.placement.viewmodel.PlacementViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PlacementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlacementPredictorTheme {
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.testTag("main_navigation_bar")
                        ) {
                            NavigationBarItem(
                                selected = uiState.selectedTab == AppTab.PREDICTOR,
                                onClick = { viewModel.selectTab(AppTab.PREDICTOR) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.selectedTab == AppTab.PREDICTOR) Icons.Filled.Psychology else Icons.Outlined.Psychology,
                                        contentDescription = "Predictor"
                                    )
                                },
                                label = { Text("Predictor") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PurplePrimary,
                                    selectedTextColor = PurplePrimary,
                                    indicatorColor = PurpleSecondary.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_predictor")
                            )

                            NavigationBarItem(
                                selected = uiState.selectedTab == AppTab.CHARTS,
                                onClick = { viewModel.selectTab(AppTab.CHARTS) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.selectedTab == AppTab.CHARTS) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                                        contentDescription = "Charts"
                                    )
                                },
                                label = { Text("Charts") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PurplePrimary,
                                    selectedTextColor = PurplePrimary,
                                    indicatorColor = PurpleSecondary.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_charts")
                            )

                            NavigationBarItem(
                                selected = uiState.selectedTab == AppTab.WHAT_IF,
                                onClick = { viewModel.selectTab(AppTab.WHAT_IF) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.selectedTab == AppTab.WHAT_IF) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                        contentDescription = "What-If"
                                    )
                                },
                                label = { Text("What-If") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PurplePrimary,
                                    selectedTextColor = PurplePrimary,
                                    indicatorColor = PurpleSecondary.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_what_if")
                            )

                            NavigationBarItem(
                                selected = uiState.selectedTab == AppTab.DATASET,
                                onClick = { viewModel.selectTab(AppTab.DATASET) },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.selectedTab == AppTab.DATASET) Icons.Filled.Storage else Icons.Outlined.Storage,
                                        contentDescription = "Dataset"
                                    )
                                },
                                label = { Text("Dataset") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PurplePrimary,
                                    selectedTextColor = PurplePrimary,
                                    indicatorColor = PurpleSecondary.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.testTag("nav_tab_dataset")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.selectedTab) {
                            AppTab.PREDICTOR -> PredictorScreen(
                                state = uiState,
                                viewModel = viewModel
                            )
                            AppTab.CHARTS -> ChartsScreen(
                                state = uiState,
                                viewModel = viewModel
                            )
                            AppTab.WHAT_IF -> WhatIfScreen(
                                state = uiState,
                                viewModel = viewModel
                            )
                            AppTab.DATASET -> DatasetExplorerScreen(
                                state = uiState,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
