package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Scale
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.ui.components.LanguagePill
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalRed
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.ui.theme.ProteinColor
import com.example.viewmodel.NutracalViewModel

@Composable
fun SettingsScreen(
    viewModel: NutracalViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var showNutritionGoalDialog by remember { mutableStateOf(false) }
    var showWeightGoalDialog by remember { mutableStateOf(false) }
    var showLogWeightDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("settings_screen")
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
                        text = "Settings & Goals",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )

                    LanguagePill(
                        currentLanguage = language,
                        onLanguageSelected = { viewModel.setLanguage(it) }
                    )
                }
            }

            // User Info Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(NutracalBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = NutracalWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.name,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Text(
                                text = "${userProfile.email} • ${userProfile.gender.displayName}, ${userProfile.age} yrs",
                                fontSize = 12.sp,
                                color = NutracalSecondary
                            )
                        }
                    }
                }
            }

            // SECTION 1: MyFitnessPal Nutrition & Macro Goals
            item {
                Text(
                    text = "Nutrition & Targets",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showNutritionGoalDialog = true }
                        .testTag("settings_nutrition_goals_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.PieChart, contentDescription = null, tint = NutracalBlack, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Calorie & Macro Goals", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = NutracalBlack)
                            }
                            Text(text = "EDIT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NutracalGreen)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Daily Calorie Budget: ${userProfile.dailyCalorieTarget} kcal",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Carbs: ${userProfile.carbsPercent}% (${userProfile.dailyCarbsTarget}g) • Protein: ${userProfile.proteinPercent}% (${userProfile.dailyProteinTarget}g) • Fat: ${userProfile.fatPercent}% (${userProfile.dailyFatTarget}g)",
                            fontSize = 12.sp,
                            color = NutracalSecondary
                        )
                    }
                }
            }

            // SECTION 2: Weight Goals & Tracking
            item {
                Text(
                    text = "Weight & Body Composition",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Scale, contentDescription = null, tint = NutracalBlack, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Weight Goal", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = NutracalBlack)
                            }
                            Text(
                                text = "EDIT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalGreen,
                                modifier = Modifier.clickable { showWeightGoalDialog = true }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Starting", fontSize = 11.sp, color = NutracalSecondary)
                                Text(text = "${userProfile.startingWeightKg} kg", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                            }
                            Column {
                                Text(text = "Current", fontSize = 11.sp, color = NutracalSecondary)
                                Text(text = "${userProfile.weightKg} kg", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                            }
                            Column {
                                Text(text = "Target", fontSize = 11.sp, color = NutracalSecondary)
                                Text(text = "${userProfile.targetWeightKg} kg", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NutracalGreen)
                            }
                            Column {
                                Text(text = "Pace", fontSize = 11.sp, color = NutracalSecondary)
                                Text(text = "-0.5 kg/wk", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NutracalAmber)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { showLogWeightDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("log_current_weight_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NutracalWhite)
                        ) {
                            Text(text = "+ Log Current Weight", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                        }
                    }
                }
            }

            // SECTION 3: Cal AI Engine & Data Management
            item {
                Text(
                    text = "App Data & System",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.clearDiary()
                                    Toast.makeText(context, "Today's diary reset", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.DeleteSweep, contentDescription = null, tint = NutracalRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Clear Today's Diary", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NutracalRed)
                        }

                        HorizontalDivider(thickness = 0.5.dp, color = NutracalCardBorder, modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "AI Composition Standard", fontSize = 13.sp, color = NutracalSecondary)
                            Text(text = "ICMR-NIN 2024", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "App Architecture", fontSize = 13.sp, color = NutracalSecondary)
                            Text(text = "Cal AI + MyFitnessPal Hybrid", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NutracalGreen)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        // Dialog 1: Nutrition & Macro Goals Editor (MyFitnessPal style)
        if (showNutritionGoalDialog) {
            EditNutritionGoalsDialog(
                currentProfile = userProfile,
                onDismiss = { showNutritionGoalDialog = false },
                onSave = { cal, pG, cG, fG, cPct, pPct, fPct ->
                    viewModel.updateNutritionGoals(cal, pG, cG, fG, cPct, pPct, fPct)
                    showNutritionGoalDialog = false
                    Toast.makeText(context, "Nutrition goals updated!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Dialog 2: Weight Goals Editor
        if (showWeightGoalDialog) {
            EditWeightGoalsDialog(
                currentProfile = userProfile,
                onDismiss = { showWeightGoalDialog = false },
                onSave = { startKg, currentKg, targetKg ->
                    viewModel.updateWeightGoals(startKg, currentKg, targetKg, -0.5f)
                    showWeightGoalDialog = false
                    Toast.makeText(context, "Weight goals updated!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Dialog 3: Quick Log Current Weight
        if (showLogWeightDialog) {
            LogWeightDialog(
                currentWeight = userProfile.weightKg,
                onDismiss = { showLogWeightDialog = false },
                onLog = { weight, note ->
                    viewModel.logWeight(weight, note)
                    showLogWeightDialog = false
                    Toast.makeText(context, "Weight logged: $weight kg", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun EditNutritionGoalsDialog(
    currentProfile: com.example.model.UserProfile,
    onDismiss: () -> Unit,
    onSave: (Int, Int, Int, Int, Int, Int, Int) -> Unit
) {
    var calories by remember { mutableStateOf(currentProfile.dailyCalorieTarget.toString()) }
    var carbsPct by remember { mutableFloatStateOf(currentProfile.carbsPercent.toFloat()) }
    var proteinPct by remember { mutableFloatStateOf(currentProfile.proteinPercent.toFloat()) }
    var fatPct by remember { mutableFloatStateOf(currentProfile.fatPercent.toFloat()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NutracalWhite),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(text = "Edit Nutrition Goals", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Daily Calorie Target") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                val totalCal = calories.toIntOrNull() ?: 2000
                val calcCarbsG = ((totalCal * (carbsPct / 100f)) / 4f).toInt()
                val calcProteinG = ((totalCal * (proteinPct / 100f)) / 4f).toInt()
                val calcFatG = ((totalCal * (fatPct / 100f)) / 9f).toInt()

                Text(text = "Carbs: ${carbsPct.toInt()}% ($calcCarbsG g)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = CarbsColor)
                Slider(
                    value = carbsPct,
                    onValueChange = { carbsPct = it },
                    valueRange = 20f..70f,
                    colors = SliderDefaults.colors(thumbColor = CarbsColor, activeTrackColor = CarbsColor)
                )

                Text(text = "Protein: ${proteinPct.toInt()}% ($calcProteinG g)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ProteinColor)
                Slider(
                    value = proteinPct,
                    onValueChange = { proteinPct = it },
                    valueRange = 15f..50f,
                    colors = SliderDefaults.colors(thumbColor = ProteinColor, activeTrackColor = ProteinColor)
                )

                Text(text = "Fat: ${fatPct.toInt()}% ($calcFatG g)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = FatColor)
                Slider(
                    value = fatPct,
                    onValueChange = { fatPct = it },
                    valueRange = 15f..50f,
                    colors = SliderDefaults.colors(thumbColor = FatColor, activeTrackColor = FatColor)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val calInt = calories.toIntOrNull() ?: 1950
                        onSave(calInt, calcProteinG, calcCarbsG, calcFatG, carbsPct.toInt(), proteinPct.toInt(), fatPct.toInt())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Text("Save Goals", fontWeight = FontWeight.Bold, color = NutracalWhite)
                }
            }
        }
    }
}

@Composable
fun EditWeightGoalsDialog(
    currentProfile: com.example.model.UserProfile,
    onDismiss: () -> Unit,
    onSave: (Float, Float, Float) -> Unit
) {
    var startingKg by remember { mutableStateOf(currentProfile.startingWeightKg.toString()) }
    var currentKg by remember { mutableStateOf(currentProfile.weightKg.toString()) }
    var targetKg by remember { mutableStateOf(currentProfile.targetWeightKg.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NutracalWhite),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(text = "Edit Weight Goals", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = startingKg,
                    onValueChange = { startingKg = it },
                    label = { Text("Starting Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = currentKg,
                    onValueChange = { currentKg = it },
                    label = { Text("Current Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = targetKg,
                    onValueChange = { targetKg = it },
                    label = { Text("Target Goal Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val s = startingKg.toFloatOrNull() ?: 74f
                        val c = currentKg.toFloatOrNull() ?: 70f
                        val t = targetKg.toFloatOrNull() ?: 66f
                        onSave(s, c, t)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Text("Save Weight Goals", fontWeight = FontWeight.Bold, color = NutracalWhite)
                }
            }
        }
    }
}

@Composable
fun LogWeightDialog(
    currentWeight: Float,
    onDismiss: () -> Unit,
    onLog: (Float, String) -> Unit
) {
    var weightInput by remember { mutableStateOf(currentWeight.toString()) }
    var note by remember { mutableStateOf("Morning weight check") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NutracalWhite),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(text = "Log Today's Weight", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val w = weightInput.toFloatOrNull() ?: currentWeight
                        onLog(w, note)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
                ) {
                    Text("Save Weight Entry", fontWeight = FontWeight.Bold, color = NutracalWhite)
                }
            }
        }
    }
}
