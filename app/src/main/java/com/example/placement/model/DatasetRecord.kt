package com.example.placement.model

data class DatasetRecord(
    val id: Int,
    val cgpa: Float,
    val marks10th: Float,
    val marks12th: Float,
    val internship: Boolean,
    val projects: Int,
    val communicationSkills: Float,
    val technicalSkills: Float,
    val backlogs: Int,
    val placed: Boolean
) {
    fun toStudentProfile(): StudentProfile {
        return StudentProfile(
            cgpa = cgpa,
            marks10th = marks10th,
            marks12th = marks12th,
            internship = internship,
            projects = projects,
            communicationSkills = communicationSkills,
            technicalSkills = technicalSkills,
            backlogs = backlogs
        )
    }
}

object PlacementDataset {
    val records: List<DatasetRecord> = listOf(
        DatasetRecord(1, 7.8f, 82f, 78f, true, 3, 8f, 8f, 0, true),
        DatasetRecord(2, 6.5f, 70f, 65f, false, 1, 6f, 5f, 2, false),
        DatasetRecord(3, 8.2f, 88f, 85f, true, 4, 9f, 9f, 0, true),
        DatasetRecord(4, 6.8f, 75f, 70f, true, 2, 7f, 6f, 1, true),
        DatasetRecord(5, 5.9f, 65f, 60f, false, 1, 5f, 4f, 3, false),
        DatasetRecord(6, 7.5f, 80f, 76f, true, 3, 8f, 7f, 0, true),
        DatasetRecord(7, 6.2f, 68f, 64f, false, 1, 6f, 5f, 2, false),
        DatasetRecord(8, 8.5f, 90f, 88f, true, 5, 9f, 9f, 0, true),
        DatasetRecord(9, 7.1f, 78f, 72f, true, 2, 7f, 7f, 1, true),
        DatasetRecord(10, 6.0f, 66f, 62f, false, 1, 5f, 5f, 2, false),
        DatasetRecord(11, 8.0f, 85f, 82f, true, 4, 8f, 9f, 0, true),
        DatasetRecord(12, 6.7f, 72f, 68f, false, 2, 6f, 6f, 1, false),
        DatasetRecord(13, 7.9f, 84f, 80f, true, 3, 8f, 8f, 0, true),
        DatasetRecord(14, 5.8f, 62f, 58f, false, 1, 4f, 4f, 3, false),
        DatasetRecord(15, 7.3f, 79f, 75f, true, 2, 8f, 7f, 0, true),
        DatasetRecord(16, 6.4f, 69f, 66f, false, 1, 6f, 5f, 2, false),
        DatasetRecord(17, 8.3f, 89f, 86f, true, 4, 9f, 9f, 0, true),
        DatasetRecord(18, 7.0f, 76f, 71f, true, 2, 7f, 6f, 1, true),
        DatasetRecord(19, 6.1f, 67f, 63f, false, 1, 5f, 5f, 2, false),
        DatasetRecord(20, 8.1f, 87f, 83f, true, 4, 9f, 8f, 0, true),
        DatasetRecord(21, 7.6f, 81f, 79f, true, 3, 8f, 8f, 0, true),
        DatasetRecord(22, 6.3f, 71f, 67f, false, 2, 6f, 5f, 2, false),
        DatasetRecord(23, 8.6f, 92f, 90f, true, 5, 10f, 10f, 0, true),
        DatasetRecord(24, 5.7f, 60f, 56f, false, 1, 4f, 4f, 3, false),
        DatasetRecord(25, 7.4f, 80f, 74f, true, 3, 8f, 7f, 0, true),
        DatasetRecord(26, 6.6f, 73f, 69f, false, 1, 6f, 6f, 1, false),
        DatasetRecord(27, 8.4f, 91f, 87f, true, 4, 9f, 9f, 0, true),
        DatasetRecord(28, 7.2f, 77f, 73f, true, 2, 7f, 7f, 0, true),
        DatasetRecord(29, 6.0f, 64f, 61f, false, 1, 5f, 4f, 2, false)
    )

    val totalRecords = records.size
    val placedCount = records.count { it.placed }
    val unplacedCount = totalRecords - placedCount
    val placementRatePercent = (placedCount.toFloat() / totalRecords) * 100f
    val averageCgpa = records.map { it.cgpa }.average().toFloat()
    val averageTechnical = records.map { it.technicalSkills }.average().toFloat()
}
