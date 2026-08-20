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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.model.DatasetRecord
import com.example.placement.model.PlacementDataset
import com.example.placement.ui.theme.ErrorRed
import com.example.placement.ui.theme.ErrorRedLight
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary
import com.example.placement.ui.theme.SuccessGreen
import com.example.placement.ui.theme.SuccessGreenLight
import com.example.placement.viewmodel.PlacementUiState
import com.example.placement.viewmodel.PlacementViewModel

enum class DatasetFilter { ALL, PLACED, UNPLACED }

@Composable
fun DatasetExplorerScreen(
    state: PlacementUiState,
    viewModel: PlacementViewModel,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(DatasetFilter.ALL) }

    val filteredRecords = remember(selectedFilter) {
        when (selectedFilter) {
            DatasetFilter.ALL -> PlacementDataset.records
            DatasetFilter.PLACED -> PlacementDataset.records.filter { it.placed }
            DatasetFilter.UNPLACED -> PlacementDataset.records.filter { !it.placed }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Dataset Summary Statistics Card
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dataset_summary_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Placement Training Dataset",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Historical campus recruitment cohort data (${PlacementDataset.totalRecords} records)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SummaryChip(
                            label = "Placement Rate",
                            value = "${PlacementDataset.placementRatePercent.toInt()}%",
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryChip(
                            label = "Avg CGPA",
                            value = String.format("%.2f", PlacementDataset.averageCgpa),
                            color = OceanBlue,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryChip(
                            label = "Avg Tech",
                            value = String.format("%.1f", PlacementDataset.averageTechnical),
                            color = PurpleSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FilterChip(
                    selected = selectedFilter == DatasetFilter.ALL,
                    onClick = { selectedFilter = DatasetFilter.ALL },
                    label = { Text("All (${PlacementDataset.records.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PurpleSecondary,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedFilter == DatasetFilter.PLACED,
                    onClick = { selectedFilter = DatasetFilter.PLACED },
                    label = { Text("Placed (${PlacementDataset.placedCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SuccessGreen,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedFilter == DatasetFilter.UNPLACED,
                    onClick = { selectedFilter = DatasetFilter.UNPLACED },
                    label = { Text("Unplaced (${PlacementDataset.unplacedCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ErrorRed,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        items(filteredRecords, key = { it.id }) { record ->
            DatasetRecordCard(
                record = record,
                onLoadRecord = { viewModel.loadDatasetRecord(record) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SummaryChip(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DatasetRecordCard(
    record: DatasetRecord,
    onLoadRecord: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dataset_record_${record.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Student #${record.id}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = if (record.placed) SuccessGreenLight else ErrorRedLight,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (record.placed) "PLACED" else "UNPLACED",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold,
                            color = if (record.placed) SuccessGreen else ErrorRed,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "CGPA: ${record.cgpa} | 10th: ${record.marks10th.toInt()}% | 12th: ${record.marks12th.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Projects: ${record.projects} | Tech: ${record.technicalSkills.toInt()} | Comm: ${record.communicationSkills.toInt()} | Backlogs: ${record.backlogs}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = onLoadRecord,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("load_record_${record.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Load", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
