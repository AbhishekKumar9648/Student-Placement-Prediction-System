package com.example.placement.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.SuccessGreen
import com.example.placement.ui.theme.WarningOrange

@Composable
fun ConfidenceGauge(
    percentage: Float,
    isPlaced: Boolean,
    modifier: Modifier = Modifier
) {
    val animPercent = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        animPercent.snapTo(0f)
        animPercent.animateTo(
            targetValue = percentage,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    val activeColor = when {
        isPlaced && percentage >= 85f -> SuccessGreen
        isPlaced -> OceanBlue
        else -> WarningOrange
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(130.dp)
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val strokeWidth = 12.dp.toPx()
            val canvasSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

            // Background track
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = canvasSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress Arc
            val sweep = (animPercent.value / 100f) * 270f
            if (sweep > 0f) {
                drawArc(
                    color = activeColor,
                    startAngle = 135f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = canvasSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "${animPercent.value.toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = activeColor
            )
            Text(
                text = "Placement Chance",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
