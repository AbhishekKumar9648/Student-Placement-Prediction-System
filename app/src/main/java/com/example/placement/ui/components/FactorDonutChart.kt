package com.example.placement.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.model.FactorScore

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FactorDonutChart(
    factors: List<FactorScore>,
    modifier: Modifier = Modifier
) {
    var selectedFactor by remember { mutableStateOf<FactorScore?>(null) }
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(factors) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    val totalWeight = factors.sumOf { it.contributionPercent.toDouble() }.toFloat().coerceAtLeast(1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("factor_donut_chart_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Factor Contribution (Pie / Donut)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Relative Weight",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Donut Chart Canvas with centered info
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = 32.dp.toPx()
                    val chartSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                    var startAngle = -90f

                    factors.forEach { factor ->
                        val sweepAngle = (factor.contributionPercent / totalWeight) * 360f * animationProgress.value
                        if (sweepAngle > 0f) {
                            val color = Color(factor.colorHex)
                            val isSelected = selectedFactor?.name == factor.name
                            drawArc(
                                color = if (selectedFactor == null || isSelected) color else color.copy(alpha = 0.35f),
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                topLeft = topLeft,
                                size = chartSize,
                                style = Stroke(
                                    width = if (isSelected) strokeWidth * 1.15f else strokeWidth,
                                    cap = StrokeCap.Butt
                                )
                            )
                            startAngle += sweepAngle
                        }
                    }
                }

                // Center Label
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    val currentFactor = selectedFactor ?: factors.maxByOrNull { it.contributionPercent }
                    if (currentFactor != null) {
                        Text(
                            text = currentFactor.name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${currentFactor.contributionPercent}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(currentFactor.colorHex),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Factor Legends
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                factors.forEach { factor ->
                    val isSelected = selectedFactor?.name == factor.name
                    val factorColor = Color(factor.colorHex)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(
                                color = if (isSelected) factorColor.copy(alpha = 0.15f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedFactor = if (isSelected) null else factor
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(factorColor, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${factor.name} (${factor.contributionPercent}%)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) factorColor else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
