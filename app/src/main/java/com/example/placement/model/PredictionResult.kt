package com.example.placement.model

data class FactorScore(
    val name: String,
    val score: Float, // 0 to 100
    val maxScore: Float = 100f,
    val contributionPercent: Float = 0f,
    val colorHex: Long = 0xFF5B2C6F
)

data class PredictionResult(
    val profile: StudentProfile,
    val isPlaced: Boolean,
    val statusText: String, // "PLACED" or "NOT PLACED"
    val placedProbability: Float, // e.g. 91.5%
    val notPlacedProbability: Float, // e.g. 8.5%
    val expectedPackageLpa: Float?, // e.g. 7.5 LPA or null
    val primaryReason: String,
    val improvementTips: List<String>,
    val factors: List<FactorScore>,
    val backlogsPenalty: Float, // 0-50
    val eligibilityPassedCount: Int,
    val totalEligibilityRules: Int = 7
)
