package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.model.GlycemicLevel
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalRed
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SugarSpikeMeter(
    level: GlycemicLevel,
    explanation: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val targetAngle = when (level) {
        GlycemicLevel.LOW -> -150f
        GlycemicLevel.MEDIUM -> -90f
        GlycemicLevel.HIGH -> -30f
    }

    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(900),
        label = "spike_gauge"
    )

    val (badgeText, badgeColor, badgeBg) = when (level) {
        GlycemicLevel.LOW -> Triple(level.labelEn, NutracalGreen, NutracalGreen.copy(alpha = 0.12f))
        GlycemicLevel.MEDIUM -> Triple(level.labelEn, NutracalAmber, NutracalAmber.copy(alpha = 0.15f))
        GlycemicLevel.HIGH -> Triple(level.labelEn, NutracalRed, NutracalRed.copy(alpha = 0.12f))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("sugar_spike_meter_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NutracalCardBg),
        elevation = CardDefaults.cardElevation(0.dp)
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
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Sugar Spike",
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = Localization.t("sugar_spike_meter", language),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalBlack
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gauge Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(width = 180.dp, height = 100.dp)
                ) {
                    val strokeW = 12.dp.toPx()
                    val arcRect = Size(size.width - strokeW, (size.width - strokeW))
                    val arcOffset = Offset(strokeW / 2, strokeW / 2)

                    // Three segments: Green (-180 to -120), Amber (-120 to -60), Red (-60 to 0)
                    drawArc(
                        color = NutracalGreen,
                        startAngle = 180f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = arcOffset,
                        size = arcRect,
                        style = Stroke(width = strokeW, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = NutracalAmber,
                        startAngle = 240f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = arcOffset,
                        size = arcRect,
                        style = Stroke(width = strokeW)
                    )
                    drawArc(
                        color = NutracalRed,
                        startAngle = 300f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = arcOffset,
                        size = arcRect,
                        style = Stroke(width = strokeW, cap = StrokeCap.Round)
                    )

                    // Draw needle indicator
                    val centerPt = Offset(size.width / 2, size.width / 2)
                    val rad = Math.toRadians(animatedAngle.toDouble())
                    val needleLen = (size.width / 2) - 18.dp.toPx()
                    val needleEnd = Offset(
                        (centerPt.x + needleLen * cos(rad)).toFloat(),
                        (centerPt.y + needleLen * sin(rad)).toFloat()
                    )

                    drawLine(
                        color = NutracalBlack,
                        start = centerPt,
                        end = needleEnd,
                        strokeWidth = 3.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    drawCircle(
                        color = NutracalBlack,
                        radius = 6.dp.toPx(),
                        center = centerPt
                    )
                    drawCircle(
                        color = NutracalWhite,
                        radius = 2.5.dp.toPx(),
                        center = centerPt
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation Text
            Text(
                text = explanation,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = NutracalBlack.copy(alpha = 0.85f)
            )
        }
    }
}
