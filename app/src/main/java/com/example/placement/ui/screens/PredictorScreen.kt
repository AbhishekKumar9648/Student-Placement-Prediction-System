package com.example.placement.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.model.Presets
import com.example.placement.ui.components.BinaryToggleField
import com.example.placement.ui.components.PredictionResultCard
import com.example.placement.ui.components.SliderInputField
import com.example.placement.ui.components.StepperInputField
import com.example.placement.ui.theme.ElectricIndigo
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary
import com.example.placement.ui.theme.WarningOrange
import com.example.placement.viewmodel.AppTab
import com.example.placement.viewmodel.PlacementUiState
import com.example.placement.viewmodel.PlacementViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PredictorScreen(
    state: PlacementUiState,
    viewModel: PlacementViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header matching Streamlit & Tkinter design
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hero_header"),
            shape = RoundedCornerShape(20.dp),
            color = PurplePrimary,
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(PurplePrimary, PurpleSecondary, ElectricIndigo)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Placement Prediction System",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Predict placement probability, CTC LPA, and factor scores based on profile metrics",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Quick Preset Profiles Selection
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Presets",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap to load sample",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Presets.list.forEach { preset ->
                    val isSelected = state.activePreset == preset.title
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.applyPreset(preset) },
                        label = { Text(preset.title) },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurpleSecondary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("preset_${preset.title.replace(" ", "_").lowercase()}")
                    )
                }
            }
        }

        // Student Input Card (The 8 Parameters)
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("student_input_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Student Input Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "8 Factors",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 1. CGPA Slider
                SliderInputField(
                    label = "CGPA",
                    value = state.profile.cgpa,
                    onValueChange = { viewModel.updateCgpa(it) },
                    valueRange = 0.0f..10.0f,
                    displayFormat = { "$it / 10.0" },
                    subtitle = "Minimum 7.0 recommended",
                    testTag = "input_cgpa"
                )

                // 2. 10th Marks Slider
                SliderInputField(
                    label = "10th Marks (%)",
                    value = state.profile.marks10th,
                    onValueChange = { viewModel.update10thMarks(it) },
                    valueRange = 0.0f..100.0f,
                    displayFormat = { "${it.toInt()}%" },
                    subtitle = "Secondary education percentage",
                    testTag = "input_10th_marks"
                )

                // 3. 12th Marks Slider
                SliderInputField(
                    label = "12th Marks (%)",
                    value = state.profile.marks12th,
                    onValueChange = { viewModel.update12thMarks(it) },
                    valueRange = 0.0f..100.0f,
                    displayFormat = { "${it.toInt()}%" },
                    subtitle = "Higher secondary percentage",
                    testTag = "input_12th_marks"
                )

                // 4. Internship Toggle
                BinaryToggleField(
                    label = "Internship Completed",
                    value = state.profile.internship,
                    onValueChange = { viewModel.updateInternship(it) },
                    subtitle = "Industrial or research internship",
                    testTag = "input_internship"
                )

                // 5. Projects Stepper
                StepperInputField(
                    label = "Number of Projects",
                    value = state.profile.projects,
                    onValueChange = { viewModel.updateProjects(it) },
                    minVal = 0,
                    maxVal = 20,
                    subtitle = "Technical hands-on builds",
                    testTag = "input_projects"
                )

                // 6. Communication Skills Slider
                SliderInputField(
                    label = "Communication Skills (0 - 10)",
                    value = state.profile.communicationSkills,
                    onValueChange = { viewModel.updateCommunicationSkills(it) },
                    valueRange = 0.0f..10.0f,
                    displayFormat = { "$it / 10" },
                    subtitle = "Interview & presentation ability",
                    testTag = "input_communication"
                )

                // 7. Technical Skills Slider
                SliderInputField(
                    label = "Technical Skills (0 - 10)",
                    value = state.profile.technicalSkills,
                    onValueChange = { viewModel.updateTechnicalSkills(it) },
                    valueRange = 0.0f..10.0f,
                    displayFormat = { "$it / 10" },
                    subtitle = "DSA, coding & core concepts",
                    testTag = "input_technical"
                )

                // 8. Backlogs Stepper
                StepperInputField(
                    label = "Active Backlogs",
                    value = state.profile.backlogs,
                    onValueChange = { viewModel.updateBacklogs(it) },
                    minVal = 0,
                    maxVal = 20,
                    subtitle = "0 backlogs required for most drives",
                    testTag = "input_backlogs"
                )
            }
        }

        // Action Buttons Row (Predict, Clear, Show Charts, Reset)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.predict() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("predict_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleSecondary)
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Predict Placement",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.clearFields() },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("clear_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = WarningOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Clear", color = WarningOrange, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.predict()
                        viewModel.selectTab(AppTab.CHARTS)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("show_charts_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Show Charts", color = Color.White, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.resetToDefaults() },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("reset_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reset")
                }
            }
        }

        // Validation Error if any
        state.validationError?.let { error ->
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Results Card
        state.predictionResult?.let { result ->
            PredictionResultCard(result = result)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
