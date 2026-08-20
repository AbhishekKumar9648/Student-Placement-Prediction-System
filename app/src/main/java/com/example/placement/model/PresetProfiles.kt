package com.example.placement.model

data class PresetProfile(
    val title: String,
    val description: String,
    val profile: StudentProfile
)

object Presets {
    val list = listOf(
        PresetProfile(
            title = "High Achiever",
            description = "Top academic marks, high technical & multiple projects",
            profile = StudentProfile(
                cgpa = 8.5f,
                marks10th = 90.0f,
                marks12th = 88.0f,
                internship = true,
                projects = 5,
                communicationSkills = 9.0f,
                technicalSkills = 9.0f,
                backlogs = 0
            )
        ),
        PresetProfile(
            title = "Average Good",
            description = "Solid academics, completed internship & good projects",
            profile = StudentProfile(
                cgpa = 7.5f,
                marks10th = 80.0f,
                marks12th = 76.0f,
                internship = true,
                projects = 3,
                communicationSkills = 8.0f,
                technicalSkills = 7.0f,
                backlogs = 0
            )
        ),
        PresetProfile(
            title = "Borderline Profile",
            description = "Acceptable CGPA but has 1 backlog and borderline projects",
            profile = StudentProfile(
                cgpa = 7.0f,
                marks10th = 76.0f,
                marks12th = 71.0f,
                internship = true,
                projects = 2,
                communicationSkills = 7.0f,
                technicalSkills = 6.0f,
                backlogs = 1
            )
        ),
        PresetProfile(
            title = "Needs Focus",
            description = "Low CGPA, no internship, multiple backlogs",
            profile = StudentProfile(
                cgpa = 5.9f,
                marks10th = 65.0f,
                marks12th = 60.0f,
                internship = false,
                projects = 1,
                communicationSkills = 5.0f,
                technicalSkills = 4.0f,
                backlogs = 3
            )
        )
    )
}
