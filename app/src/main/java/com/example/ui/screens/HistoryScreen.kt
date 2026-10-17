package com.example.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.data.local.MealEntity
import com.example.model.AppLanguage
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalRed
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.viewmodel.NutracalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: NutracalViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val allMeals by viewModel.allMeals.collectAsState()

    val totalCalories = allMeals.sumOf { it.calories }
    val totalProtein = allMeals.sumOf { it.proteinG.toDouble() }.toFloat()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("history_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Localization.t("tab_history", language),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NutracalCardBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${allMeals.size} logged meals",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NutracalSecondary
                        )
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Total Logged", fontSize = 12.sp, color = NutracalSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "$totalCalories kcal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Total Protein", fontSize = 12.sp, color = NutracalSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "${totalProtein.toInt()}g", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalGreen)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Avg Spike", fontSize = 12.sp, color = NutracalSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "Optimal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalAmber)
                        }
                    }
                }
            }

            if (allMeals.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = null,
                            tint = NutracalSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No meals logged yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NutracalSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your scanned thalis will appear here.",
                            fontSize = 13.sp,
                            color = NutracalSecondary.copy(alpha = 0.8f)
                        )
                    }
                }
            } else {
                items(allMeals, key = { it.id }) { meal ->
                    HistoryMealCard(
                        meal = meal,
                        onDelete = { viewModel.deleteMeal(meal) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
fun HistoryMealCard(
    meal: MealEntity,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("EEE, dd MMM • hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(meal.timestamp) { dateFormat.format(Date(meal.timestamp)) }

    val thumbnailBitmap = remember(meal.thumbnailBase64) {
        if (!meal.thumbnailBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(meal.thumbnailBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_meal_${meal.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = NutracalSecondary
                )

                val (spikeText, spikeColor) = when (meal.glycemicLevel) {
                    "LOW" -> Pair("Low Spike", NutracalGreen)
                    "HIGH" -> Pair("High Spike", NutracalRed)
                    else -> Pair("Moderate", NutracalAmber)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(spikeColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = spikeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = spikeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (thumbnailBitmap != null) {
                    Image(
                        bitmap = thumbnailBitmap.asImageBitmap(),
                        contentDescription = "Meal Thumbnail",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, NutracalWhite, RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NutracalWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Restaurant,
                            contentDescription = null,
                            tint = NutracalBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meal.mealTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalBlack,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${meal.calories} kcal • P: ${meal.proteinG.toInt()}g • C: ${meal.carbsG.toInt()}g • F: ${meal.fatG.toInt()}g",
                        fontSize = 12.sp,
                        color = NutracalSecondary
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Delete",
                        tint = NutracalSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
