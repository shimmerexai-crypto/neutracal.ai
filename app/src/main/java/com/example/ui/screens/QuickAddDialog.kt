package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.IndianFoodDatabase
import com.example.data.PresetFood
import com.example.model.AppLanguage
import com.example.model.MealType
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite

@Composable
fun QuickAddDialog(
    language: AppLanguage,
    initialMealType: MealType = MealType.LUNCH,
    onDismiss: () -> Unit,
    onAddFood: (PresetFood, MealType, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember(searchQuery) {
        IndianFoodDatabase.search(searchQuery)
    }

    var selectedFood by remember { mutableStateOf<PresetFood?>(searchResults.firstOrNull()) }
    var selectedMealType by remember { mutableStateOf(initialMealType) }
    var portionScale by remember { mutableFloatStateOf(1.0f) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("quick_add_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NutracalWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Add Food",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = NutracalSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Box with Autocomplete
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search bajre ki roti, gatte ki sabzi...") },
                    leadingIcon = {
                        Icon(Icons.Rounded.Search, contentDescription = null, tint = NutracalSecondary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quick_add_search_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NutracalBlack,
                        unfocusedBorderColor = NutracalCardBorder,
                        focusedContainerColor = NutracalCardBg,
                        unfocusedContainerColor = NutracalCardBg
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Popular Pill Suggestions
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val quickChips = listOf("Bajre Ki Roti", "Gatte Ki Sabzi", "Dal Baati", "Ker Sangri", "Paneer")
                    items(quickChips) { chip ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NutracalCardBg)
                                .clickable { searchQuery = chip }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = chip,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = NutracalBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Autocomplete List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 180.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(searchResults) { food ->
                        val isSelected = selectedFood == food
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFood = food }
                                .testTag("food_option_${food.nameEn.lowercase().replace(" ", "_")}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) NutracalBlack else NutracalCardBg
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = food.nameEn,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) NutracalWhite else NutracalBlack
                                    )
                                    Text(
                                        text = food.servingDesc,
                                        fontSize = 11.sp,
                                        color = if (isSelected) NutracalWhite.copy(alpha = 0.7f) else NutracalSecondary
                                    )
                                }
                                Text(
                                    text = "${food.calories} kcal",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NutracalGreen else NutracalBlack
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selected Food Details & Portion multiplier
                selectedFood?.let { food ->
                    val cal = (food.calories * portionScale).toInt()
                    val p = food.protein * portionScale
                    val c = food.carbs * portionScale
                    val f = food.fat * portionScale

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Portion: $cal kcal",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Text(
                            text = "P: ${p.toInt()}g • C: ${c.toInt()}g • F: ${f.toInt()}g",
                            fontSize = 11.sp,
                            color = NutracalSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Portion chips: 0.5x, 1x, 1.5x, 2x
                    val scales = listOf(0.5f to "0.5x", 1.0f to "1.0x", 1.5f to "1.5x", 2.0f to "2.0x")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        scales.forEach { (scale, label) ->
                            val isSel = portionScale == scale
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) NutracalBlack else NutracalCardBg)
                                    .clickable { portionScale = scale }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSel) NutracalWhite else NutracalBlack
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add Button
                Button(
                    onClick = {
                        selectedFood?.let { food ->
                            onAddFood(food, selectedMealType, portionScale)
                        }
                    },
                    enabled = selectedFood != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quick_add_confirm_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Text(
                        text = "Add to Diary",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalWhite
                    )
                }
            }
        }
    }
}
