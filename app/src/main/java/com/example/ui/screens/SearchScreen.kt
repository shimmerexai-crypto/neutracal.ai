package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Verified
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.IndianFoodDatabase
import com.example.data.PresetFood
import com.example.model.AppLanguage
import com.example.model.GlycemicLevel
import com.example.model.MealType
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.viewmodel.NutracalViewModel

@Composable
fun SearchScreen(
    viewModel: NutracalViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

    var showCustomFoodDialog by remember { mutableStateOf(false) }
    var showBarcodeScanSuccess by remember { mutableStateOf(false) }

    val allPresetItems = remember { IndianFoodDatabase.items }
    val searchResults = remember(searchQuery, selectedCategory) {
        val list = if (searchQuery.isNotBlank()) IndianFoodDatabase.search(searchQuery) else allPresetItems
        if (selectedCategory == "All") list else list.filter { it.category.contains(selectedCategory, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("search_screen")
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
                        text = "Food Database",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NutracalCardBg)
                            .clickable { showCustomFoodDialog = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("create_custom_food_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = NutracalBlack, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Custom", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                        }
                    }
                }
            }

            // Meal Type Selector Pills (Breakfast, Lunch, Snacks, Dinner)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MealType.values().forEach { mType ->
                        val isSel = selectedMealType == mType
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) NutracalBlack else NutracalCardBg)
                                .clickable { selectedMealType = mType }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mType.labelEn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSel) NutracalWhite else NutracalBlack,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Search Bar & Barcode scan simulation
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search bajre ki roti, dal baati...", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(Icons.Rounded.Search, contentDescription = null, tint = NutracalSecondary)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_food_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NutracalBlack,
                            unfocusedBorderColor = NutracalCardBorder,
                            focusedContainerColor = NutracalCardBg,
                            unfocusedContainerColor = NutracalCardBg
                        )
                    )

                    // Barcode button (MyFitnessPal signature feature)
                    IconButton(
                        onClick = {
                            showBarcodeScanSuccess = true
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NutracalCardBg)
                            .testTag("barcode_scanner_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QrCodeScanner,
                            contentDescription = "Scan Barcode",
                            tint = NutracalBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Category Filter Chips
            item {
                val categories = listOf("All", "Breads & Roti", "Sabzi & Curries", "Dals & Legumes", "Rice & Grains", "Rajasthani Specialty", "Breakfast")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSel = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) NutracalBlack else NutracalCardBg)
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) NutracalWhite else NutracalBlack
                            )
                        }
                    }
                }
            }

            // Food List Items
            items(searchResults) { food ->
                SearchFoodCard(
                    food = food,
                    onLogFood = { portion ->
                        viewModel.quickAddPresetFood(food, selectedMealType, portion)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        // Custom Food Creator Dialog
        if (showCustomFoodDialog) {
            CustomFoodCreatorDialog(
                onDismiss = { showCustomFoodDialog = false },
                onCreate = { customFood ->
                    viewModel.quickAddPresetFood(customFood, selectedMealType, 1.0f)
                    showCustomFoodDialog = false
                }
            )
        }

        // Barcode Scanned Success Dialog
        if (showBarcodeScanSuccess) {
            BarcodeDetectedDialog(
                onDismiss = { showBarcodeScanSuccess = false },
                onLog = { scannedFood ->
                    viewModel.quickAddPresetFood(scannedFood, selectedMealType, 1.0f)
                    showBarcodeScanSuccess = false
                }
            )
        }
    }
}

@Composable
fun SearchFoodCard(
    food: PresetFood,
    onLogFood: (Float) -> Unit
) {
    var portion by remember { mutableFloatStateOf(1.0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("search_food_${food.nameEn.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(18.dp),
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
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = food.nameEn,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = "ICMR-NIN Verified",
                            tint = NutracalGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "${food.servingDesc} • ${food.category}",
                        fontSize = 12.sp,
                        color = NutracalSecondary
                    )
                }

                Text(
                    text = "${(food.calories * portion).toInt()} kcal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NutracalBlack
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "P: ${(food.protein * portion).toInt()}g • C: ${(food.carbs * portion).toInt()}g • F: ${(food.fat * portion).toInt()}g",
                    fontSize = 12.sp,
                    color = NutracalSecondary
                )

                // Quick portion pills: 1x, 2x
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(1.0f to "1x", 1.5f to "1.5x", 2.0f to "2x").forEach { (sc, lbl) ->
                        val isSel = portion == sc
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) NutracalBlack else NutracalWhite)
                                .clickable { portion = sc }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = lbl,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSel) NutracalWhite else NutracalBlack
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NutracalGreen)
                            .clickable { onLogFood(portion) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+ LOG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomFoodCreatorDialog(
    onDismiss: () -> Unit,
    onCreate: (PresetFood) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var serving by remember { mutableStateOf("1 serving (150g)") }
    var calories by remember { mutableStateOf("250") }
    var protein by remember { mutableStateOf("10") }
    var carbs by remember { mutableStateOf("30") }
    var fat by remember { mutableStateOf("8") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NutracalWhite),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Create Custom Dish",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name (e.g. Bajra Khichdi)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = serving,
                    onValueChange = { serving = it },
                    label = { Text("Serving description") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Protein (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Carbs (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("Fat (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val item = PresetFood(
                            nameEn = if (name.isNotBlank()) name else "Custom Homemade Meal",
                            nameHi = if (name.isNotBlank()) name else "कस्टम भोजन",
                            nameHinglish = if (name.isNotBlank()) name else "Custom Desi Meal",
                            servingDesc = serving,
                            calories = calories.toIntOrNull() ?: 250,
                            protein = protein.toFloatOrNull() ?: 10f,
                            carbs = carbs.toFloatOrNull() ?: 30f,
                            fat = fat.toFloatOrNull() ?: 8f,
                            glycemicLevel = GlycemicLevel.LOW,
                            category = "Custom Meals",
                            defaultHealthFix = "Homemade healthy dish. Pair with fresh salad and curd."
                        )
                        onCreate(item)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Text("Save & Log to Diary", fontWeight = FontWeight.Bold, color = NutracalWhite)
                }
            }
        }
    }
}

@Composable
fun BarcodeDetectedDialog(
    onDismiss: () -> Unit,
    onLog: (PresetFood) -> Unit
) {
    val detectedFood = remember {
        PresetFood(
            nameEn = "Amul Taaza Homogenised Toned Milk",
            nameHi = "अमूल ताज़ा टोन्ड दूध",
            nameHinglish = "Amul Taaza Milk (200ml)",
            servingDesc = "1 tetra pack (200 ml)",
            calories = 116,
            protein = 6.2f,
            carbs = 9.4f,
            fat = 6.0f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Dairy & Beverages",
            defaultHealthFix = "Rich in calcium and casein protein. Great for post-workout or evening chai."
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NutracalWhite),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(NutracalGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = NutracalGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Barcode Identified!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
                Text(
                    text = "8901262010052 • Verified Product",
                    fontSize = 12.sp,
                    color = NutracalSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = detectedFood.nameEn,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Text(
                            text = detectedFood.servingDesc,
                            fontSize = 12.sp,
                            color = NutracalSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${detectedFood.calories} kcal • P: ${detectedFood.protein}g • C: ${detectedFood.carbs}g • F: ${detectedFood.fat}g",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NutracalBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { onLog(detectedFood) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Text("Add to Log", fontWeight = FontWeight.Bold, color = NutracalWhite)
                }
            }
        }
    }
}
