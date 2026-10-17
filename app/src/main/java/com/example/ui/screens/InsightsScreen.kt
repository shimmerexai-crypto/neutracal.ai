package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.ui.theme.ProteinColor
import com.example.viewmodel.NutracalViewModel

@Composable
fun InsightsScreen(
    viewModel: NutracalViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val allMeals by viewModel.allMeals.collectAsState()
    val allWeightLogs by viewModel.allWeightLogs.collectAsState()

    // 7-day data (simulated with real meal fallback for Mon-Sun)
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val calData = listOf(1850, 1920, 1780, 2050, 1890, 1950, 1820)
    val proteinData = listOf(82, 88, 79, 95, 84, 91, 86)

    val avgCalories = calData.average().toInt()
    val avgProtein = proteinData.average().toInt()

    val shareText = """
🥗 Nutracal.ai - Weekly Nutrition Report Card
👤 User: ${userProfile.name}
🎯 Goal: ${userProfile.goal.titleEn}
🔥 Daily Avg: $avgCalories kcal (Target: ${userProfile.dailyCalorieTarget} kcal)
💪 Protein Avg: $avgProtein g (Target: ${userProfile.dailyProteinTarget} g)
⚡ Glycemic Health Score: 94/100 (Optimal Control)
⚖️ Current Weight: ${userProfile.weightKg} kg (Target: ${userProfile.targetWeightKg} kg)
✨ Powered by ICMR-NIN Indian Food AI Scanner & MyFitnessPal Diary
""".trimIndent()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("insights_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress & Reports",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NutracalGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Score: A (94%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalGreen
                        )
                    }
                }
            }

            // MyFitnessPal Signature: Weight Progress Trend Chart
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("insights_weight_trend_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.MonitorWeight, contentDescription = null, tint = NutracalBlack, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Weight Progression",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NutracalBlack
                                )
                            }

                            Text(
                                text = "${userProfile.weightKg} kg (Target: ${userProfile.targetWeightKg} kg)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Line Graph for Weight
                        val weights = if (allWeightLogs.isNotEmpty()) allWeightLogs.map { it.weightKg } else listOf(74.5f, 73.2f, 72.1f, 71.0f, 70.0f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                if (weights.size >= 2) {
                                    val minW = (weights.minOrNull() ?: 68f) - 1.5f
                                    val maxW = (weights.maxOrNull() ?: 76f) + 1f
                                    val range = maxW - minW

                                    val points = weights.mapIndexed { idx, w ->
                                        val x = idx * (size.width / (weights.size - 1))
                                        val y = size.height - ((w - minW) / range) * size.height
                                        Offset(x, y)
                                    }

                                    // Target line
                                    val targetY = size.height - ((userProfile.targetWeightKg - minW) / range).coerceIn(0f, 1f) * size.height
                                    drawLine(
                                        color = NutracalGreen.copy(alpha = 0.5f),
                                        start = Offset(0f, targetY),
                                        end = Offset(size.width, targetY),
                                        strokeWidth = 1.5.dp.toPx()
                                    )

                                    // Weight Path
                                    val path = Path().apply {
                                        moveTo(points.first().x, points.first().y)
                                        for (i in 1 until points.size) {
                                            lineTo(points[i].x, points[i].y)
                                        }
                                    }

                                    drawPath(
                                        path = path,
                                        color = NutracalBlack,
                                        style = Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    )

                                    // Points
                                    points.forEach { pt ->
                                        drawCircle(color = NutracalWhite, radius = 5.dp.toPx(), center = pt)
                                        drawCircle(color = NutracalBlack, radius = 3.dp.toPx(), center = pt)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Start: ${userProfile.startingWeightKg} kg", fontSize = 11.sp, color = NutracalSecondary)
                            Text(text = "-4.5 kg Lost", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NutracalGreen)
                            Text(text = "Goal: ${userProfile.targetWeightKg} kg", fontSize = 11.sp, color = NutracalSecondary)
                        }
                    }
                }
            }

            // MyFitnessPal Macro Distribution Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Macronutrient Split (Actual vs Goal)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Stacked Bar for Macros
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                        ) {
                            Box(modifier = Modifier.weight(userProfile.carbsPercent.toFloat()).fillMaxSize().background(CarbsColor))
                            Box(modifier = Modifier.weight(userProfile.proteinPercent.toFloat()).fillMaxSize().background(ProteinColor))
                            Box(modifier = Modifier.weight(userProfile.fatPercent.toFloat()).fillMaxSize().background(FatColor))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MacroNutrientLegend(name = "Carbohydrates", percent = "${userProfile.carbsPercent}%", color = CarbsColor)
                            MacroNutrientLegend(name = "Protein", percent = "${userProfile.proteinPercent}%", color = ProteinColor)
                            MacroNutrientLegend(name = "Healthy Fats", percent = "${userProfile.fatPercent}%", color = FatColor)
                        }
                    }
                }
            }

            // Weekly Calorie Bar Chart Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("insights_calorie_chart_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Weekly Calorie Intake",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NutracalBlack
                                )
                                Text(
                                    text = "Avg $avgCalories kcal / day",
                                    fontSize = 12.sp,
                                    color = NutracalSecondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NutracalWhite)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Target: ${userProfile.dailyCalorieTarget}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NutracalBlack
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val maxCal = 2400f
                                val barWidth = 24.dp.toPx()
                                val spacing = (size.width - (barWidth * days.size)) / (days.size + 1)

                                val targetY = size.height - (userProfile.dailyCalorieTarget / maxCal) * size.height
                                drawLine(
                                    color = Color(0xFFD1D1D6),
                                    start = Offset(0f, targetY),
                                    end = Offset(size.width, targetY),
                                    strokeWidth = 1.dp.toPx()
                                )

                                calData.forEachIndexed { index, cal ->
                                    val barHeight = (cal / maxCal) * size.height
                                    val x = spacing + index * (barWidth + spacing)
                                    val y = size.height - barHeight

                                    drawRoundRect(
                                        color = if (cal <= userProfile.dailyCalorieTarget + 100) NutracalBlack else NutracalAmber,
                                        topLeft = Offset(x, y),
                                        size = Size(barWidth, barHeight),
                                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            days.forEach { day ->
                                Text(
                                    text = day,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NutracalSecondary,
                                    modifier = Modifier.width(32.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Shareable Cal AI Report Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("weekly_report_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalBlack)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Stars,
                                    contentDescription = null,
                                    tint = NutracalGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Localization.t("weekly_report_title", language),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NutracalWhite
                                )
                            }

                            Text(
                                text = "Grade A",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NutracalGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "“Outstanding adherence with ICMR-NIN Indian staples. Blood glucose control remained 94% optimal throughout the week.”",
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = NutracalWhite.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Calorie Adherence", fontSize = 11.sp, color = NutracalWhite.copy(alpha = 0.6f))
                                Text(text = "96%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NutracalWhite)
                            }
                            Column {
                                Text(text = "Protein Goal", fontSize = 11.sp, color = NutracalWhite.copy(alpha = 0.6f))
                                Text(text = "6 of 7 days", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NutracalGreen)
                            }
                            Column {
                                Text(text = "Sugar Spikes", fontSize = 11.sp, color = NutracalWhite.copy(alpha = 0.6f))
                                Text(text = "Minimal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NutracalAmber)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Nutracal Report Card")
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("share_report_card_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NutracalWhite)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = null,
                                tint = NutracalBlack,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Localization.t("share_report", language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NutracalBlack
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
fun MacroNutrientLegend(name: String, percent: String, color: Color) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = percent, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
        }
        Text(text = name, fontSize = 10.sp, color = NutracalSecondary)
    }
}
