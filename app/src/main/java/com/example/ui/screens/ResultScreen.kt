package com.example.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.FoodItem
import com.example.model.MealType
import com.example.ui.components.SugarSpikeMeter
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.ui.theme.ProteinColor
import com.example.viewmodel.NutracalViewModel

@Composable
fun ResultScreen(
    viewModel: NutracalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val language by viewModel.language.collectAsState()
    val scanResult by viewModel.activeScanResult.collectAsState()

    var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

    if (scanResult == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(NutracalWhite),
            contentAlignment = Alignment.Center
        ) {
            Text("No scan data available.", color = NutracalSecondary)
        }
        return
    }

    val result = scanResult!!

    // Decode thumbnail if present
    val thumbnailBitmap = remember(result.imageBase64) {
        if (!result.imageBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(result.imageBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("result_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NutracalCardBg)
                            .testTag("result_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = NutracalBlack
                        )
                    }

                    Text(
                        text = Localization.t("result_title", language),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NutracalGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${result.confidence}% Match",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalGreen
                        )
                    }
                }
            }

            // Thumbnail and Calorie Hero Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("result_calories_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (thumbnailBitmap != null) {
                            Image(
                                bitmap = thumbnailBitmap.asImageBitmap(),
                                contentDescription = "Plate Thumbnail",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, NutracalWhite, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text(
                            text = "${result.totalCalories}",
                            fontSize = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NutracalBlack,
                            letterSpacing = (-1.5).sp
                        )
                        Text(
                            text = "total calories (kcal)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = NutracalSecondary
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Macro Bars
                        MacroProgressBar(
                            label = "Protein",
                            amount = "${result.totalProteinG.toInt()}g",
                            color = ProteinColor,
                            percent = (result.totalProteinG / 50f).coerceIn(0f, 1f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        MacroProgressBar(
                            label = "Carbohydrates",
                            amount = "${result.totalCarbsG.toInt()}g",
                            color = CarbsColor,
                            percent = (result.totalCarbsG / 120f).coerceIn(0f, 1f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        MacroProgressBar(
                            label = "Fats & Ghee",
                            amount = "${result.totalFatG.toInt()}g",
                            color = FatColor,
                            percent = (result.totalFatG / 45f).coerceIn(0f, 1f)
                        )
                    }
                }
            }

            // Sugar Spike Meter Gauge Card
            item {
                SugarSpikeMeter(
                    level = result.glycemicLoadLevel,
                    explanation = result.spikeExplanation,
                    language = language
                )
            }

            // Desi Health Fix Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("desi_health_fix_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NutracalGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Spa,
                                contentDescription = "Fix",
                                tint = NutracalGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = Localization.t("desi_health_fix", language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = result.desiHealthFix,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = NutracalBlack.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // Portion Confirm Section (Live multiplier adjustments: 0.5x, 1x, 1.5x, 2x)
            item {
                Text(
                    text = Localization.t("portion_confirm", language),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
            }

            itemsIndexed(result.items) { index, item ->
                PortionItemCard(
                    item = item,
                    onPortionChanged = { newScale ->
                        viewModel.updateItemPortion(index, newScale)
                    }
                )
            }

            // Meal Type Selector
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select Meal Type",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MealType.values().forEach { mType ->
                        val isSel = selectedMealType == mType
                        val label = when (language) {
                            AppLanguage.HINDI -> mType.labelHi
                            AppLanguage.HINGLISH -> mType.labelHinglish
                            AppLanguage.ENGLISH -> mType.labelEn
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) NutracalBlack else NutracalCardBg)
                                .clickable { selectedMealType = mType }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSel) NutracalWhite else NutracalBlack,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Disclaimer
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = NutracalSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Localization.t("disclaimer", language),
                        fontSize = 11.sp,
                        color = NutracalSecondary
                    )
                }
            }

            // Log Button
            item {
                Button(
                    onClick = { viewModel.logCurrentScanResult(selectedMealType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("result_log_meal_btn"),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = NutracalWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.t("log_meal", language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalWhite
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PortionItemCard(
    item: FoodItem,
    onPortionChanged: (Float) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("portion_card_${item.name.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalBlack
                    )
                    Text(
                        text = item.estimatedQuantity,
                        fontSize = 12.sp,
                        color = NutracalSecondary
                    )
                }

                Text(
                    text = "${item.calories} kcal",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Portion Multiplier Pills: ½x, 1x, 1.5x, 2x
            val portions = listOf(0.5f to "½x", 1.0f to "1x", 1.5f to "1.5x", 2.0f to "2x")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                portions.forEach { (scale, label) ->
                    val isSel = item.portionScale == scale
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) NutracalBlack else NutracalWhite)
                            .clickable { onPortionChanged(scale) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) NutracalWhite else NutracalBlack
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MacroProgressBar(
    label: String,
    amount: String,
    color: androidx.compose.ui.graphics.Color,
    percent: Float
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percent,
        animationSpec = tween(600),
        label = "macro_progress"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = NutracalSecondary)
            Text(text = amount, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedPercent },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFFE5E5EA)
        )
    }
}
