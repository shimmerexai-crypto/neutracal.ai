package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.ui.theme.ProteinColor

@Composable
fun CalorieRing(
    consumedCalories: Int,
    targetCalories: Int,
    consumedProtein: Float,
    targetProtein: Int,
    consumedCarbs: Float,
    targetCarbs: Int,
    consumedFat: Float,
    targetFat: Int,
    isDetailed: Boolean,
    onToggleMode: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val remainingCalories = maxOf(0, targetCalories - consumedCalories)
    val calProgress = (consumedCalories.toFloat() / maxOf(1, targetCalories)).coerceIn(0f, 1.25f)
    val proteinProgress = (consumedProtein / maxOf(1, targetProtein)).coerceIn(0f, 1.25f)
    val carbsProgress = (consumedCarbs / maxOf(1, targetCarbs)).coerceIn(0f, 1.25f)
    val fatProgress = (consumedFat / maxOf(1, targetFat)).coerceIn(0f, 1.25f)

    val animatedCal by animateFloatAsState(targetValue = calProgress, animationSpec = tween(700), label = "cal")
    val animatedProtein by animateFloatAsState(targetValue = proteinProgress, animationSpec = tween(700), label = "protein")
    val animatedCarbs by animateFloatAsState(targetValue = carbsProgress, animationSpec = tween(700), label = "carbs")
    val animatedFat by animateFloatAsState(targetValue = fatProgress, animationSpec = tween(700), label = "fat")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("calorie_ring_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NutracalCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode toggle button (Simple vs Detailed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Localization.t("app_name", language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalSecondary,
                    letterSpacing = 1.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NutracalWhite)
                        .clickable { onToggleMode() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("toggle_ring_mode_btn")
                ) {
                    Text(
                        text = if (isDetailed) Localization.t("ring_detailed", language)
                               else Localization.t("ring_simple", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rings Container
            Box(
                modifier = Modifier.size(210.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    val strokeWidth = if (isDetailed) 9.dp.toPx() else 14.dp.toPx()
                    val centerOffset = Offset(size.width / 2, size.height / 2)

                    if (!isDetailed) {
                        // Simple Mode: Single bold calorie ring
                        val radius = (size.width - strokeWidth) / 2
                        val arcSize = Size(radius * 2, radius * 2)
                        val arcOffset = Offset((size.width - radius * 2) / 2, (size.height - radius * 2) / 2)

                        // Track
                        drawArc(
                            color = Color(0xFFE5E5EA),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = arcOffset,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Progress
                        drawArc(
                            color = NutracalBlack,
                            startAngle = -90f,
                            sweepAngle = (animatedCal.coerceAtMost(1.0f) * 360f),
                            useCenter = false,
                            topLeft = arcOffset,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    } else {
                        // Detailed Mode: Concentric rings (Calories, Protein, Carbs, Fat)
                        val rings = listOf(
                            Triple(animatedCal, NutracalBlack, 0.dp.toPx()),
                            Triple(animatedProtein, ProteinColor, 14.dp.toPx()),
                            Triple(animatedCarbs, CarbsColor, 28.dp.toPx()),
                            Triple(animatedFat, FatColor, 42.dp.toPx())
                        )

                        rings.forEach { (progress, color, inset) ->
                            val radius = (size.width - strokeWidth) / 2 - inset
                            val arcSize = Size(radius * 2, radius * 2)
                            val arcOffset = Offset(centerOffset.x - radius, centerOffset.y - radius)

                            // Track
                            drawArc(
                                color = color.copy(alpha = 0.18f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = arcOffset,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                            // Value
                            drawArc(
                                color = color,
                                startAngle = -90f,
                                sweepAngle = (progress.coerceAtMost(1.0f) * 360f),
                                useCenter = false,
                                topLeft = arcOffset,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                    }
                }

                // Center Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$remainingCalories",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = Localization.t("calories_left", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = NutracalSecondary
                    )
                    Text(
                        text = "Goal: $targetCalories kcal",
                        fontSize = 11.sp,
                        color = NutracalSecondary.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Macro summary chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroBadge(
                    label = "Protein",
                    current = "${consumedProtein.toInt()}g",
                    target = "${targetProtein}g",
                    color = ProteinColor
                )
                MacroBadge(
                    label = "Carbs",
                    current = "${consumedCarbs.toInt()}g",
                    target = "${targetCarbs}g",
                    color = CarbsColor
                )
                MacroBadge(
                    label = "Fats",
                    current = "${consumedFat.toInt()}g",
                    target = "${targetFat}g",
                    color = FatColor
                )
            }
        }
    }
}

@Composable
fun MacroBadge(
    label: String,
    current: String,
    target: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = NutracalSecondary
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = current,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = NutracalBlack
        )
        Text(
            text = "/ $target",
            fontSize = 10.sp,
            color = NutracalSecondary
        )
    }
}
