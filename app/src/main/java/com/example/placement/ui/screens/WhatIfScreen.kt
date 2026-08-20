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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.placement.engine.PlacementPredictionEngine
import com.example.placement.model.StudentProfile
import com.example.placement.ui.components.ConfidenceGauge
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary
import com.example.placement.ui.theme.SuccessGreen
import com.example.placement.ui.theme.WarningOrange
import com.example.placement.viewmodel.PlacementUiState
import com.example.placement.viewmodel.PlacementViewModel

@Composable
fun WhatIfScreen(
    state: PlacementUiState,
    viewModel: PlacementViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val baseProfile = state.profile
    val baseResult = state.predictionResult ?: PlacementPredictionEngine.predict(baseProfile)

    // Simulate potential improvements
    val clearedBacklogsProfile = baseProfile.copy(backlogs = 0)
    val clearedBacklogsResult = PlacementPredictionEngine.predict(clearedBacklogsProfile)

    val boostedCgpaProfile = baseProfile.copy(cgpa = (baseProfile.cgpa + 0.8f).coerceAtMost(10.0f))
    val boostedCgpaResult = PlacementPredictionEngine.predict(boostedCgpaProfile)

    val boostedTechProfile = baseProfile.copy(technicalSkills = (baseProfile.technicalSkills + 2.0f).coerceAtMost(10.0f))
    val boostedTechResult = PlacementPredictionEngine.predict(boostedTechProfile)

    val completedInternshipProfile = baseProfile.copy(internship = true, projects = baseProfile.projects.coerceAtLeast(3))
    val completedInternshipResult = PlacementPredictionEngine.predict(completedInternshipProfile)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("what_if_header_card"),
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
                        text = "What-If Simulator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "See how changing key factors impacts placement probability and package",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ConfidenceGauge(
                    percentage = baseResult.placedProbability,
                    isPlaced = baseResult.isPlaced,
                    modifier = Modifier.size(90.dp)
                )
            }
        }

        Text(
            text = "Optimization Scenarios",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Scenario 1: Clear Backlogs (if currently has backlogs)
        if (baseProfile.backlogs > 0) {
            SimulationScenarioCard(
                title = "Clear All Active Backlogs",
                description = "Resolving backlogs from ${baseProfile.backlogs} to 0",
                currentProb = baseResult.placedProbability,
                simulatedProb = clearedBacklogsResult.placedProbability,
                currentPkg = baseResult.expectedPackageLpa,
                simulatedPkg = clearedBacklogsResult.expectedPackageLpa,
                onApply = {
                    viewModel.updateBacklogs(0)
                    viewModel.predict()
                }
            )
        }

        // Scenario 2: Boost Technical Skills
        SimulationScenarioCard(
            title = "Boost Technical Skills (+2.0)",
            description = "Intensive DSA and coding practice from ${baseProfile.technicalSkills} to ${boostedTechProfile.technicalSkills}",
            currentProb = baseResult.placedProbability,
            simulatedProb = boostedTechResult.placedProbability,
            currentPkg = baseResult.expectedPackageLpa,
            simulatedPkg = boostedTechResult.expectedPackageLpa,
            onApply = {
                viewModel.updateTechnicalSkills(boostedTechProfile.technicalSkills)
                viewModel.predict()
            }
        )

        // Scenario 3: Raise CGPA
        SimulationScenarioCard(
            title = "Raise CGPA by +0.8",
            description = "Target higher semester grades from ${baseProfile.cgpa} to ${boostedCgpaProfile.cgpa}",
            currentProb = baseResult.placedProbability,
            simulatedProb = boostedCgpaResult.placedProbability,
            currentPkg = baseResult.expectedPackageLpa,
            simulatedPkg = boostedCgpaResult.expectedPackageLpa,
            onApply = {
                viewModel.updateCgpa(boostedCgpaProfile.cgpa)
                viewModel.predict()
            }
        )

        // Scenario 4: Internship & Projects
        if (!baseProfile.internship || baseProfile.projects < 3) {
            SimulationScenarioCard(
                title = "Complete Internship & 3+ Projects",
                description = "Gain industrial experience and build functional software projects",
                currentProb = baseResult.placedProbability,
                simulatedProb = completedInternshipResult.placedProbability,
                currentPkg = baseResult.expectedPackageLpa,
                simulatedPkg = completedInternshipResult.expectedPackageLpa,
                onApply = {
                    viewModel.updateInternship(true)
                    viewModel.updateProjects(baseProfile.projects.coerceAtLeast(3))
                    viewModel.predict()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SimulationScenarioCard(
    title: String,
    description: String,
    currentProb: Float,
    simulatedProb: Float,
    currentPkg: Float?,
    simulatedPkg: Float?,
    onApply: () -> Unit
) {
    val deltaProb = simulatedProb - currentProb
    val isPositive = deltaProb >= 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    color = if (isPositive) SuccessGreen.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (deltaProb >= 0) "+${deltaProb.toInt()}% Chance" else "${deltaProb.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isPositive) SuccessGreen else Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Simulated Outcome:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${simulatedProb.toInt()}% Chance • ${simulatedPkg?.let { "₹$it LPA" } ?: "Needs more criteria"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (simulatedPkg != null) SuccessGreen else WarningOrange
                    )
                }

                OutlinedButton(
                    onClick = onApply,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Apply Scenario", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
