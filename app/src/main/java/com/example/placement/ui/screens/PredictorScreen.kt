package com.example.placement.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.model.Presets
import com.example.placement.ui.components.PredictionResultCard
import com.example.placement.ui.components.SmartPlacementGraphicCard
import com.example.placement.ui.theme.ElectricIndigo
import com.example.placement.ui.theme.ErrorRed
import com.example.placement.ui.theme.ErrorRedLight
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary
import com.example.placement.ui.theme.SuccessGreen
import com.example.placement.ui.theme.SuccessGreenLight
import com.example.placement.ui.theme.WarningOrange
import com.example.placement.viewmodel.AppTab
import com.example.placement.viewmodel.PlacementUiState
import com.example.placement.viewmodel.PlacementViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PredictorScreen(
    state: PlacementUiState,
    viewModel: PlacementViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Top Header: Student Placement Prediction System banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("app_header_banner"),
            shape = RoundedCornerShape(10.dp),
            color = PurplePrimary,
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF4A148C), Color(0xFF6A1B9A), Color(0xFF4A148C))
                        )
                    )
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Student Placement Prediction System",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Predict your placement chances based on your profile",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Quick Preset Profiles Selection (Fast sample loading)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Presets:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A148C)
                )
                Text(
                    text = "Tap to auto-fill",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Presets.list.forEach { preset ->
                    val isSelected = state.activePreset == preset.title
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.applyPreset(preset) },
                        label = { Text(preset.title, fontSize = 12.sp) },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
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

        // 2. Student Input Details Card (Framed exactly like Tkinter screenshot)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("student_input_details_card"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.5.dp, Color(0xFF5B2C6F)),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Framed Box Label: "Student Input Details"
                Text(
                    text = "Student Input Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5B2C6F)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2-Column Grid of Text Entry Fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Column (CGPA, 10th Marks, 12th Marks, Internship)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TkinterInputField(
                            label = "CGPA",
                            value = if (state.profile.cgpa == 0f && state.activePreset == null && state.predictionResult == null) "" else state.profile.cgpa.toString(),
                            onValueChange = { str ->
                                val v = str.toFloatOrNull() ?: 0f
                                viewModel.updateCgpa(v.coerceIn(0f, 10f))
                            },
                            placeholder = "e.g. 7.8",
                            testTag = "input_cgpa"
                        )

                        TkinterInputField(
                            label = "10th Marks (%)",
                            value = if (state.profile.marks10th == 0f && state.activePreset == null && state.predictionResult == null) "" else state.profile.marks10th.toInt().toString(),
                            onValueChange = { str ->
                                val v = str.toFloatOrNull() ?: 0f
                                viewModel.update10thMarks(v.coerceIn(0f, 100f))
                            },
                            placeholder = "e.g. 85",
                            testTag = "input_10th_marks"
                        )

                        TkinterInputField(
                            label = "12th Marks (%)",
                            value = if (state.profile.marks12th == 0f && state.activePreset == null && state.predictionResult == null) "" else state.profile.marks12th.toInt().toString(),
                            onValueChange = { str ->
                                val v = str.toFloatOrNull() ?: 0f
                                viewModel.update12thMarks(v.coerceIn(0f, 100f))
                            },
                            placeholder = "e.g. 80",
                            testTag = "input_12th_marks"
                        )

                        TkinterInputField(
                            label = "Internship (1 = Yes, 0 = No)",
                            value = if (state.profile.internship) "1" else "0",
                            onValueChange = { str ->
                                val isYes = str.trim() == "1" || str.trim().lowercase() == "yes"
                                viewModel.updateInternship(isYes)
                            },
                            placeholder = "1 or 0",
                            testTag = "input_internship"
                        )
                    }

                    // Right Column (Projects, Communication, Technical Skills, Backlogs)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TkinterInputField(
                            label = "Number of Projects",
                            value = if (state.profile.projects == 0 && state.activePreset == null && state.predictionResult == null) "" else state.profile.projects.toString(),
                            onValueChange = { str ->
                                val v = str.toIntOrNull() ?: 0
                                viewModel.updateProjects(v.coerceIn(0, 50))
                            },
                            placeholder = "e.g. 3",
                            testTag = "input_projects"
                        )

                        TkinterInputField(
                            label = "Communication (0-10)",
                            value = if (state.profile.communicationSkills == 0f && state.activePreset == null && state.predictionResult == null) "" else state.profile.communicationSkills.toString(),
                            onValueChange = { str ->
                                val v = str.toFloatOrNull() ?: 0f
                                viewModel.updateCommunicationSkills(v.coerceIn(0f, 10f))
                            },
                            placeholder = "e.g. 7.5",
                            testTag = "input_communication"
                        )

                        TkinterInputField(
                            label = "Technical Skills (0-10)",
                            value = if (state.profile.technicalSkills == 0f && state.activePreset == null && state.predictionResult == null) "" else state.profile.technicalSkills.toString(),
                            onValueChange = { str ->
                                val v = str.toFloatOrNull() ?: 0f
                                viewModel.updateTechnicalSkills(v.coerceIn(0f, 10f))
                            },
                            placeholder = "e.g. 8.0",
                            testTag = "input_technical"
                        )

                        TkinterInputField(
                            label = "Backlogs",
                            value = if (state.profile.backlogs == 0 && state.activePreset == null && state.predictionResult == null) "0" else state.profile.backlogs.toString(),
                            onValueChange = { str ->
                                val v = str.toIntOrNull() ?: 0
                                viewModel.updateBacklogs(v.coerceIn(0, 20))
                            },
                            placeholder = "e.g. 0",
                            testTag = "input_backlogs"
                        )
                    }
                }
            }
        }

        // 3. Action Buttons Stack & Smart Placement Graphic Card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Action Buttons matching exact colors & labels from Screenshot 98
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Button 1: Predict Placement (Purple)
                Button(
                    onClick = { viewModel.predict() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("predict_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C3483))
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Predict Placement",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                // Button 2: Clear (Orange)
                Button(
                    onClick = { viewModel.clearFields() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("clear_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22))
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Clear",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                // Button 3: Show Charts (Blue)
                Button(
                    onClick = {
                        viewModel.predict()
                        viewModel.selectTab(AppTab.CHARTS)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("show_charts_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2980B9))
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Show Charts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                // Button 4: Exit / Reset (Red)
                Button(
                    onClick = { viewModel.resetToDefaults() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("exit_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Exit / Reset",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }

            // Right-Side Graphic Card: Smart Placement Prediction Model
            SmartPlacementGraphicCard(
                modifier = Modifier.weight(0.9f)
            )
        }

        // Validation Error Alert if any
        state.validationError?.let { error ->
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        // 4. Prediction Results Card (Exact Framed layout matching Tkinter screenshot)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("prediction_results_card"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.5.dp, Color(0xFF5B2C6F)),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Framed Box Label: "Prediction Results"
                Text(
                    text = "Prediction Results",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5B2C6F)
                )

                Spacer(modifier = Modifier.height(14.dp))

                val result = state.predictionResult

                // 3 Columns: Prediction | Placement Chance | Expected Package
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Column 1: Prediction
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Prediction",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (result != null) result.statusText else "---",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = if (result == null) Color.Gray else if (result.isPlaced) SuccessGreen else ErrorRed
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (result != null) {
                                if (result.isPlaced) "Eligible for Drive" else "Requires Improvement"
                            } else "Waiting for prediction",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Column 2: Placement Chance
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Placement Chance",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (result != null) "${result.placedProbability} %" else "-- %",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = if (result == null) Color.Gray else OceanBlue
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Placement chance progress bar
                        LinearProgressIndicator(
                            progress = {
                                if (result != null) (result.placedProbability / 100f).coerceIn(0f, 1f) else 0f
                            },
                            modifier = Modifier
                                .width(80.dp)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (result?.isPlaced == true) SuccessGreen else WarningOrange,
                            trackColor = Color(0xFFE0E0E0)
                        )
                    }

                    // Column 3: Expected Package
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Expected Package",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (result?.expectedPackageLpa != null) "${result.expectedPackageLpa} LPA" else "-- LPA",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = if (result?.expectedPackageLpa != null) Color(0xFF6C3483) else Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (result?.expectedPackageLpa != null) "Estimated CTC" else "Not Applicable",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // If result is present, display full diagnostic details & tips
                result?.let {
                    Spacer(modifier = Modifier.height(14.dp))
                    PredictionResultCard(result = it)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TkinterInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E50),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(3.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 12.sp, color = Color.LightGray) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF5B2C6F),
                unfocusedBorderColor = Color(0xFFBDC3C7)
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag(testTag)
        )
    }
}
