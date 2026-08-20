package com.example.placement.viewmodel

import androidx.lifecycle.ViewModel
import com.example.placement.engine.PlacementPredictionEngine
import com.example.placement.model.DatasetRecord
import com.example.placement.model.PlacementDataset
import com.example.placement.model.PredictionResult
import com.example.placement.model.PresetProfile
import com.example.placement.model.Presets
import com.example.placement.model.StudentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppTab(val title: String) {
    PREDICTOR("Predictor"),
    CHARTS("Charts"),
    WHAT_IF("What-If"),
    DATASET("Dataset")
}

data class PlacementUiState(
    val profile: StudentProfile = StudentProfile(),
    val predictionResult: PredictionResult? = null,
    val selectedTab: AppTab = AppTab.PREDICTOR,
    val savedProfiles: List<Pair<String, PredictionResult>> = emptyList(),
    val activePreset: String? = null,
    val validationError: String? = null
)

class PlacementViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlacementUiState())
    val uiState: StateFlow<PlacementUiState> = _uiState.asStateFlow()

    init {
        // Run initial prediction with default profile
        predict()
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateCgpa(value: Float) {
        val rounded = ((value * 10f).toInt()) / 10f
        _uiState.update { it.copy(profile = it.profile.copy(cgpa = rounded), activePreset = null) }
    }

    fun update10thMarks(value: Float) {
        val rounded = ((value * 10f).toInt()) / 10f
        _uiState.update { it.copy(profile = it.profile.copy(marks10th = rounded), activePreset = null) }
    }

    fun update12thMarks(value: Float) {
        val rounded = ((value * 10f).toInt()) / 10f
        _uiState.update { it.copy(profile = it.profile.copy(marks12th = rounded), activePreset = null) }
    }

    fun updateInternship(value: Boolean) {
        _uiState.update { it.copy(profile = it.profile.copy(internship = value), activePreset = null) }
    }

    fun updateProjects(value: Int) {
        _uiState.update { it.copy(profile = it.profile.copy(projects = value), activePreset = null) }
    }

    fun updateCommunicationSkills(value: Float) {
        val rounded = ((value * 10f).toInt()) / 10f
        _uiState.update { it.copy(profile = it.profile.copy(communicationSkills = rounded), activePreset = null) }
    }

    fun updateTechnicalSkills(value: Float) {
        val rounded = ((value * 10f).toInt()) / 10f
        _uiState.update { it.copy(profile = it.profile.copy(technicalSkills = rounded), activePreset = null) }
    }

    fun updateBacklogs(value: Int) {
        _uiState.update { it.copy(profile = it.profile.copy(backlogs = value), activePreset = null) }
    }

    fun applyPreset(preset: PresetProfile) {
        _uiState.update {
            it.copy(
                profile = preset.profile,
                activePreset = preset.title,
                validationError = null
            )
        }
        predict()
    }

    fun loadDatasetRecord(record: DatasetRecord) {
        _uiState.update {
            it.copy(
                profile = record.toStudentProfile(),
                activePreset = "Student #${record.id}",
                validationError = null
            )
        }
        predict()
        selectTab(AppTab.PREDICTOR)
    }

    fun predict() {
        val (isValid, errorMsg) = _uiState.value.profile.isValid()
        if (!isValid) {
            _uiState.update { it.copy(validationError = errorMsg) }
            return
        }

        val result = PlacementPredictionEngine.predict(_uiState.value.profile)
        _uiState.update {
            it.copy(
                predictionResult = result,
                validationError = null
            )
        }
    }

    fun clearFields() {
        _uiState.update {
            it.copy(
                profile = StudentProfile(
                    cgpa = 0.0f,
                    marks10th = 0.0f,
                    marks12th = 0.0f,
                    internship = false,
                    projects = 0,
                    communicationSkills = 0.0f,
                    technicalSkills = 0.0f,
                    backlogs = 0
                ),
                predictionResult = null,
                activePreset = null,
                validationError = null
            )
        }
    }

    fun resetToDefaults() {
        _uiState.update {
            it.copy(
                profile = StudentProfile(),
                activePreset = null,
                validationError = null
            )
        }
        predict()
    }

    fun saveCurrentPrediction(tag: String) {
        val result = _uiState.value.predictionResult ?: return
        _uiState.update {
            val updated = it.savedProfiles.toMutableList()
            updated.add(0, (if (tag.isBlank()) "Profile #${updated.size + 1}" else tag) to result)
            it.copy(savedProfiles = updated)
        }
    }
}
