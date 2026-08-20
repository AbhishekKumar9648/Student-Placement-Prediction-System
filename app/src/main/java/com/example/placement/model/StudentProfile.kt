package com.example.placement.model

data class StudentProfile(
    val cgpa: Float = 7.5f,
    val marks10th: Float = 80.0f,
    val marks12th: Float = 78.0f,
    val internship: Boolean = true,
    val projects: Int = 3,
    val communicationSkills: Float = 7.0f,
    val technicalSkills: Float = 7.0f,
    val backlogs: Int = 0
) {
    fun isValid(): Pair<Boolean, String?> {
        if (cgpa !in 0.0f..10.0f) return false to "CGPA must be between 0.0 and 10.0"
        if (marks10th !in 0.0f..100.0f) return false to "10th Marks must be between 0 and 100%"
        if (marks12th !in 0.0f..100.0f) return false to "12th Marks must be between 0 and 100%"
        if (projects < 0) return false to "Projects cannot be negative"
        if (communicationSkills !in 0.0f..10.0f) return false to "Communication Skills must be between 0 and 10"
        if (technicalSkills !in 0.0f..10.0f) return false to "Technical Skills must be between 0 and 10"
        if (backlogs < 0) return false to "Backlogs cannot be negative"
        return true to null
    }
}
