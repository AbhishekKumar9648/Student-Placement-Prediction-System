package com.example.placement.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.ui.components.ConfidenceGauge
import com.example.placement.ui.components.FactorBarChart
import com.example.placement.ui.components.FactorDonutChart
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary
import com.example.placement.ui.theme.SuccessGreen
import com.example.placement.viewmodel.PlacementUiState
import com.example.placement.viewmodel.PlacementViewModel

@Composable
fun ChartsScreen(
    state: PlacementUiState,
    viewModel: PlacementViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val result = state.predictionResult

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Overview Banner
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("charts_overview_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Analytics & Visualization",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Multi-dimensional breakdown of student placement drivers",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (result != null) {
                    ConfidenceGauge(
                        percentage = result.placedProbability,
                        isPlaced = result.isPlaced,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        if (result != null) {
            // Chart 1: Factor Contribution Pie / Donut
            FactorDonutChart(factors = result.factors)

            // Chart 2: Factor Scores Bar Chart
            FactorBarChart(
                factors = result.factors,
                backlogsPenalty = result.backlogsPenalty
            )

            // Salary Brackets Reference Card (from original get_package logic)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("package_brackets_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Package Brackets (LPA Matrix)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = PurpleSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val brackets = listOf(
                        "95% - 100%" to "₹12.0 LPA (Tier-1 Super Dream)",
                        "90% - 94%" to "₹10.0 LPA (Dream Company)",
                        "85% - 89%" to "₹9.0 LPA (High Growth Tech)",
                        "80% - 84%" to "₹7.5 LPA (Core Technology)",
                        "75% - 79%" to "₹6.0 LPA (Standard Tech)",
                        "70% - 74%" to "₹5.2 LPA (IT Services)",
                        "65% - 69%" to "₹4.5 LPA (Associate Engineer)",
                        "60% - 64%" to "₹4.0 LPA (Base Placement)",
                        "< 60%" to "Not Estimated (Needs Profile Upgrade)"
                    )

                    brackets.forEach { (range, pkg) ->
                        val isCurrentTier = when {
                            result.placedProbability >= 95f && range.startsWith("95") -> true
                            result.placedProbability >= 90f && result.placedProbability < 95f && range.startsWith("90") -> true
                            result.placedProbability >= 85f && result.placedProbability < 90f && range.startsWith("85") -> true
                            result.placedProbability >= 80f && result.placedProbability < 85f && range.startsWith("80") -> true
                            result.placedProbability >= 75f && result.placedProbability < 80f && range.startsWith("75") -> true
                            result.placedProbability >= 70f && result.placedProbability < 75f && range.startsWith("70") -> true
                            result.placedProbability >= 65f && result.placedProbability < 70f && range.startsWith("65") -> true
                            result.placedProbability >= 60f && result.placedProbability < 65f && range.startsWith("60") -> true
                            result.placedProbability < 60f && range.startsWith("<") -> true
                            else -> false
                        }

                        Surface(
                            color = if (isCurrentTier) PurpleSecondary.copy(alpha = 0.15f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = range,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isCurrentTier) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrentTier) PurplePrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = pkg,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isCurrentTier) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isCurrentTier) PurpleSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No prediction data yet",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Run a prediction from the Predictor tab to view detailed charts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
