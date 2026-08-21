package com.example.placement.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placement.ui.theme.ElectricIndigo
import com.example.placement.ui.theme.OceanBlue
import com.example.placement.ui.theme.PurplePrimary
import com.example.placement.ui.theme.PurpleSecondary

@Composable
fun SmartPlacementGraphicCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("smart_placement_graphic_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Graphic Canvas representing Graduation Cap, Lightbulb & Open Book
            Canvas(
                modifier = Modifier
                    .size(width = 160.dp, height = 150.dp)
                    .padding(8.dp)
            ) {
                val w = size.width
                val h = size.height

                // Sparkles (4 corner decorative diamond stars)
                fun drawDiamondStar(cx: Float, cy: Float, radius: Float, color: Color) {
                    val path = Path().apply {
                        moveTo(cx, cy - radius)
                        lineTo(cx + radius * 0.4f, cy)
                        lineTo(cx, cy + radius)
                        lineTo(cx - radius * 0.4f, cy)
                        close()
                    }
                    drawPath(path, color)
                    val path2 = Path().apply {
                        moveTo(cx - radius, cy)
                        lineTo(cx, cy + radius * 0.4f)
                        lineTo(cx + radius, cy)
                        lineTo(cx, cy - radius * 0.4f)
                        close()
                    }
                    drawPath(path2, color)
                }

                // Sparkle 1: Top Right (Blue)
                drawDiamondStar(w * 0.9f, h * 0.15f, 9f, Color(0xFF3498DB))
                // Sparkle 2: Top Left (Purple)
                drawDiamondStar(w * 0.12f, h * 0.22f, 7f, Color(0xFF9B59B6))
                // Sparkle 3: Mid Left (Orange)
                drawDiamondStar(w * 0.08f, h * 0.55f, 8f, Color(0xFFE67E22))
                // Sparkle 4: Mid Right (Green)
                drawDiamondStar(w * 0.92f, h * 0.6f, 8f, Color(0xFF2ECC71))

                // 1. Lightbulb (Center glowing gold circle and bulb base)
                val bulbCenter = Offset(w * 0.5f, h * 0.24f)
                val bulbRadius = w * 0.18f

                // Bulb glow & circle
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFEB3B), Color(0xFFF1C40F), Color(0xFFD4AC0D)),
                        center = bulbCenter,
                        radius = bulbRadius
                    ),
                    radius = bulbRadius,
                    center = bulbCenter
                )
                // Bulb Base
                drawRoundRect(
                    color = Color(0xFFB7950B),
                    topLeft = Offset(w * 0.45f, bulbCenter.y + bulbRadius * 0.7f),
                    size = Size(w * 0.1f, h * 0.07f),
                    cornerRadius = CornerRadius(3f, 3f)
                )

                // 2. Graduation Cap (Diamond polygon resting under bulb)
                val capPath = Path().apply {
                    moveTo(w * 0.5f, h * 0.35f) // top point
                    lineTo(w * 0.88f, h * 0.49f) // right point
                    lineTo(w * 0.5f, h * 0.63f) // bottom point
                    lineTo(w * 0.12f, h * 0.49f) // left point
                    close()
                }
                drawPath(
                    path = capPath,
                    color = Color(0xFF6C3483)
                )

                // Cap rim underside
                val capRimPath = Path().apply {
                    moveTo(w * 0.28f, h * 0.55f)
                    lineTo(w * 0.5f, h * 0.65f)
                    lineTo(w * 0.72f, h * 0.55f)
                    lineTo(w * 0.72f, h * 0.60f)
                    lineTo(w * 0.5f, h * 0.70f)
                    lineTo(w * 0.28f, h * 0.60f)
                    close()
                }
                drawPath(
                    path = capRimPath,
                    color = Color(0xFF4A235A)
                )

                // Tassel hanging down with small orange bead
                drawLine(
                    color = Color(0xFFE67E22),
                    start = Offset(w * 0.5f, h * 0.49f),
                    end = Offset(w * 0.5f, h * 0.75f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
                drawCircle(
                    color = Color(0xFFE67E22),
                    radius = 5f,
                    center = Offset(w * 0.5f, h * 0.75f)
                )

                // 3. Open Book Base (Two blue curved pages at the bottom)
                val bookLeftPage = Path().apply {
                    moveTo(w * 0.5f, h * 0.92f)
                    cubicTo(w * 0.42f, h * 0.82f, w * 0.26f, h * 0.82f, w * 0.18f, h * 0.87f)
                    lineTo(w * 0.18f, h * 0.98f)
                    cubicTo(w * 0.26f, h * 0.93f, w * 0.42f, h * 0.93f, w * 0.5f, h * 0.98f)
                    close()
                }
                drawPath(
                    path = bookLeftPage,
                    color = Color(0xFF2980B9)
                )
                drawPath(
                    path = bookLeftPage,
                    color = Color(0xFF1B4F72),
                    style = Stroke(width = 2f)
                )

                val bookRightPage = Path().apply {
                    moveTo(w * 0.5f, h * 0.92f)
                    cubicTo(w * 0.58f, h * 0.82f, w * 0.74f, h * 0.82f, w * 0.82f, h * 0.87f)
                    lineTo(w * 0.82f, h * 0.98f)
                    cubicTo(w * 0.74f, h * 0.93f, w * 0.58f, h * 0.93f, w * 0.5f, h * 0.98f)
                    close()
                }
                drawPath(
                    path = bookRightPage,
                    color = Color(0xFF3498DB)
                )
                drawPath(
                    path = bookRightPage,
                    color = Color(0xFF1B4F72),
                    style = Stroke(width = 2f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Smart Placement",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary
            )
            Text(
                text = "Prediction Model",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = PurpleSecondary
            )
        }
    }
}
