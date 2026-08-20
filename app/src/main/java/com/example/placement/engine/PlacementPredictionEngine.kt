package com.example.placement.engine

import com.example.placement.model.FactorScore
import com.example.placement.model.PredictionResult
import com.example.placement.model.StudentProfile
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object PlacementPredictionEngine {

    fun predict(profile: StudentProfile): PredictionResult {
        val cgpa = profile.cgpa
        val marks10 = profile.marks10th
        val marks12 = profile.marks12th
        val internship = if (profile.internship) 1 else 0
        val projects = profile.projects
        val communication = profile.communicationSkills
        val technical = profile.technicalSkills
        val backlogs = profile.backlogs

        // Calculate individual factor scores (0-100 scale)
        val cgpaScore = (cgpa * 10f).coerceIn(0f, 100f)
        val internScore = if (internship == 1) 100f else 0f
        val projectScore = (min(projects, 5) * 20f).coerceIn(0f, 100f)
        val communicationScore = (communication * 10f).coerceIn(0f, 100f)
        val technicalScore = (technical * 10f).coerceIn(0f, 100f)
        val backlogsPenalty = (min(backlogs, 5) * 10f).coerceIn(0f, 50f)

        // Evaluate eligibility conditions
        val passCgpa = cgpa >= 7.0f
        val pass10th = marks10 >= 60.0f
        val pass12th = marks12 >= 60.0f
        val passProjects = projects >= 2
        val passComm = communication >= 5.0f
        val passTech = technical >= 3.0f
        val passBacklogs = backlogs == 0

        val passedRules = listOf(
            passCgpa,
            pass10th,
            pass12th,
            passProjects,
            passComm,
            passTech,
            passBacklogs
        ).count { it }

        val isEligible = passedRules == 7

        val percentage: Float
        val packageLpa: Float?
        val primaryReason: String
        val tips = mutableListOf<String>()

        if (isEligible) {
            // High placement chance for eligible students (85% - 95%)
            val rawPercent = 85f + (cgpa - 7f) * 5f + (technical / 10f) * 2f
            percentage = max(85f, min(95f, (rawPercent * 10f).roundToInt() / 10f))
            
            // Expected package between 4.0 and 8.0+ LPA based on score
            packageLpa = ((4.0f + (percentage - 85f) * 0.5f) * 10f).roundToInt() / 10f
            primaryReason = "Congratulations! All academic and skill eligibility standards met."

            if (percentage < 90f) {
                tips.add("Aim to increase technical skills above 8.5 for Tier-1 company shortlisting.")
            }
            if (projects < 4) {
                tips.add("Building 1-2 more full-stack or domain projects can elevate your package to 10+ LPA.")
            }
            if (cgpa < 8.0f) {
                tips.add("Pushing CGPA above 8.0 unlocks elite consulting and product company criteria.")
            }
            if (tips.isEmpty()) {
                tips.add("Excellent profile! Continue mock technical interviews and system design practice.")
            }
        } else {
            // Low placement chance for students failing conditions (10% - 40%)
            val rawPercent = 10f + (cgpa * 2f) + (technical * 2f) + (communication * 1f)
            percentage = max(10f, min(40f, (rawPercent * 10f).roundToInt() / 10f))
            packageLpa = null

            // Determine root cause
            primaryReason = when {
                !passBacklogs -> "Backlogs detected (${backlogs} active). Most recruitment drives require 0 active backlogs."
                !passCgpa -> "CGPA is $cgpa (Minimum requirement is 7.0 for placement rounds)."
                !passProjects -> "Only $projects project(s) listed (Minimum 2 required to showcase hands-on capability)."
                !passTech -> "Technical score is $technical/10 (Minimum 3.0 required)."
                !passComm -> "Communication score is $communication/10 (Minimum 5.0 required for HR/GD rounds)."
                !pass10th -> "10th marks are $marks10% (Minimum 60% standard)."
                !pass12th -> "12th marks are $marks12% (Minimum 60% standard)."
                else -> "Profile needs improvement across multiple metrics."
            }

            if (!passBacklogs) {
                tips.add("Priority #1: Clear all active backlogs before placement registration window closes.")
            }
            if (!passCgpa) {
                tips.add("Raise CGPA to at least 7.0 in upcoming semester examinations.")
            }
            if (!passProjects) {
                tips.add("Complete at least 2 functional real-world projects with GitHub documentation.")
            }
            if (internship == 0) {
                tips.add("Secure an industrial internship or summer training to gain practical experience.")
            }
            if (!passTech) {
                tips.add("Practice Data Structures, Algorithms, and Core CS Fundamentals on LeetCode/HackerRank.")
            }
            if (!passComm) {
                tips.add("Join group discussions and English communication workshops to boost presentation confidence.")
            }
        }

        // Compute positive sum for proportional factor contribution (excluding backlogs)
        val rawFactors = listOf(
            FactorScore("CGPA", cgpaScore, 100f, colorHex = 0xFF5B2C6F),
            FactorScore("10th Marks", marks10, 100f, colorHex = 0xFF3498DB),
            FactorScore("12th Marks", marks12, 100f, colorHex = 0xFF27AE60),
            FactorScore("Internship", internScore, 100f, colorHex = 0xFFE67E22),
            FactorScore("Projects", projectScore, 100f, colorHex = 0xFF8E44AD),
            FactorScore("Communication", communicationScore, 100f, colorHex = 0xFF2980B9),
            FactorScore("Technical", technicalScore, 100f, colorHex = 0xFFF1C40F)
        )

        val totalSum = rawFactors.sumOf { it.score.toDouble() }.toFloat()
        val factorsWithWeights = rawFactors.map {
            val weight = if (totalSum > 0f) (it.score / totalSum) * 100f else 0f
            it.copy(contributionPercent = (weight * 10f).roundToInt() / 10f)
        }

        val placedProb = percentage
        val notPlacedProb = ((100f - percentage) * 10f).roundToInt() / 10f

        return PredictionResult(
            profile = profile,
            isPlaced = isEligible,
            statusText = if (isEligible) "PLACED" else "NOT PLACED",
            placedProbability = placedProb,
            notPlacedProbability = notPlacedProb,
            expectedPackageLpa = packageLpa,
            primaryReason = primaryReason,
            improvementTips = tips,
            factors = factorsWithWeights,
            backlogsPenalty = backlogsPenalty,
            eligibilityPassedCount = passedRules
        )
    }

    fun getPackageLpaFromPercentage(percent: Float): Float {
        return when {
            percent >= 95f -> 12.0f
            percent >= 90f -> 10.0f
            percent >= 85f -> 9.0f
            percent >= 80f -> 7.5f
            percent >= 75f -> 6.0f
            percent >= 70f -> 5.2f
            percent >= 65f -> 4.5f
            percent >= 60f -> 4.0f
            else -> 0.0f
        }
    }
}
