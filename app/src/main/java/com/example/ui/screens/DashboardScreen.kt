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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BakeryDining
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.data.local.MealEntity
import com.example.model.AppLanguage
import com.example.model.MealType
import com.example.ui.components.CalorieRing
import com.example.ui.components.LanguagePill
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalRed
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.ui.theme.WaterColor
import com.example.viewmodel.NutracalViewModel

@Composable
fun DashboardScreen(
    viewModel: NutracalViewModel,
    onNavigateToScan: () -> Unit,
    onOpenProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val allMeals by viewModel.allMeals.collectAsState()
    val ringMode by viewModel.ringMode.collectAsState()
    val waterGlasses by viewModel.waterGlasses.collectAsState()

    val consumedCalories = allMeals.sumOf { it.calories }
    val consumedProtein = allMeals.sumOf { it.proteinG.toDouble() }.toFloat()
    val consumedCarbs = allMeals.sumOf { it.carbsG.toDouble() }.toFloat()
    val consumedFat = allMeals.sumOf { it.fatG.toDouble() }.toFloat()

    val exerciseBurn = userProfile.exerciseCaloriesBurnedToday
    val remainingCalories = (userProfile.dailyCalorieTarget - consumedCalories + exerciseBurn).coerceAtLeast(0)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .testTag("dashboard_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Top Bar: Profile avatar, Streak badge (Cal AI), Language Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            onOpenProfile()
                            viewModel.setDrawerOpen(true)
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(NutracalCardBg)
                            .testTag("dashboard_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Profile",
                            tint = NutracalBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Cal AI style Streak Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(NutracalCardBg)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Whatshot,
                                contentDescription = "Streak",
                                tint = NutracalAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userProfile.streakDays} Day Streak",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                        }
                    }

                    LanguagePill(
                        currentLanguage = language,
                        onLanguageSelected = { viewModel.setLanguage(it) }
                    )
                }
            }

            // MyFitnessPal Signature Calorie Equation Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mfp_equation_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EquationColumn(label = Localization.t("base_goal", language), value = "${userProfile.dailyCalorieTarget}")
                        Text(text = "-", fontSize = 18.sp, color = NutracalSecondary, fontWeight = FontWeight.Bold)
                        EquationColumn(label = Localization.t("food_eaten", language), value = "$consumedCalories")
                        Text(text = "+", fontSize = 18.sp, color = NutracalSecondary, fontWeight = FontWeight.Bold)
                        EquationColumn(label = Localization.t("exercise_burned", language), value = "$exerciseBurn", highlightColor = NutracalGreen)
                        Text(text = "=", fontSize = 18.sp, color = NutracalSecondary, fontWeight = FontWeight.Bold)
                        EquationColumn(label = Localization.t("remaining_cal", language), value = "$remainingCalories", isBold = true)
                    }
                }
            }

            // Concentric Calorie Ring (Cal AI style with Simple vs Detailed toggle)
            item {
                CalorieRing(
                    consumedCalories = consumedCalories,
                    targetCalories = userProfile.dailyCalorieTarget,
                    consumedProtein = consumedProtein,
                    targetProtein = userProfile.dailyProteinTarget,
                    consumedCarbs = consumedCarbs,
                    targetCarbs = userProfile.dailyCarbsTarget,
                    consumedFat = consumedFat,
                    targetFat = userProfile.dailyFatTarget,
                    isDetailed = ringMode == "detailed",
                    onToggleMode = { viewModel.toggleRingMode() },
                    language = language
                )
            }

            // Hero Banner: "Thali Scan Karein" (Cal AI camera trigger)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onNavigateToScan() }
                        .testTag("thali_scan_hero_banner"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalBlack)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(NutracalGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CAL AI + ICMR ENGINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NutracalGreen,
                                    letterSpacing = 0.8.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = Localization.t("banner_title", language),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalWhite,
                                letterSpacing = (-0.3).sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Localization.t("banner_sub", language),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = NutracalWhite.copy(alpha = 0.75f)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(NutracalWhite),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CenterFocusStrong,
                                contentDescription = "Scan",
                                tint = NutracalBlack,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // MyFitnessPal Meal-by-Meal Diary Sections (Breakfast, Lunch, Snacks, Dinner)
            item {
                Text(
                    text = "Meals Diary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutracalBlack
                )
            }

            // Breakfast Card
            item {
                MealSectionCard(
                    mealType = MealType.BREAKFAST,
                    title = MealType.BREAKFAST.labelEn,
                    icon = Icons.Rounded.WbTwilight,
                    meals = allMeals.filter { it.mealType.equals(MealType.BREAKFAST.name, ignoreCase = true) },
                    onAddFood = { viewModel.openQuickAddForMeal(MealType.BREAKFAST) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
            }

            // Lunch Card
            item {
                MealSectionCard(
                    mealType = MealType.LUNCH,
                    title = MealType.LUNCH.labelEn,
                    icon = Icons.Rounded.WbSunny,
                    meals = allMeals.filter { it.mealType.equals(MealType.LUNCH.name, ignoreCase = true) },
                    onAddFood = { viewModel.openQuickAddForMeal(MealType.LUNCH) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
            }

            // Snacks Card
            item {
                MealSectionCard(
                    mealType = MealType.SNACKS,
                    title = MealType.SNACKS.labelEn,
                    icon = Icons.Rounded.BakeryDining,
                    meals = allMeals.filter { it.mealType.equals(MealType.SNACKS.name, ignoreCase = true) },
                    onAddFood = { viewModel.openQuickAddForMeal(MealType.SNACKS) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
            }

            // Dinner Card
            item {
                MealSectionCard(
                    mealType = MealType.DINNER,
                    title = MealType.DINNER.labelEn,
                    icon = Icons.Rounded.NightsStay,
                    meals = allMeals.filter { it.mealType.equals(MealType.DINNER.name, ignoreCase = true) },
                    onAddFood = { viewModel.openQuickAddForMeal(MealType.DINNER) },
                    onDeleteMeal = { viewModel.deleteMeal(it) }
                )
            }

            // Exercise / Steps Card (MyFitnessPal signature)
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(NutracalGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.DirectionsRun,
                                    contentDescription = null,
                                    tint = NutracalGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Daily Exercise & Steps",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NutracalBlack
                                )
                                Text(
                                    text = "${userProfile.stepCountToday} / ${userProfile.stepGoal} steps",
                                    fontSize = 12.sp,
                                    color = NutracalSecondary
                                )
                            }
                        }

                        Text(
                            text = "-${userProfile.exerciseCaloriesBurnedToday} kcal",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalGreen
                        )
                    }
                }
            }

            // Water Tracker Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("water_tracker_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.WaterDrop,
                                    contentDescription = "Water",
                                    tint = WaterColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = Localization.t("water_tracker", language),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NutracalBlack
                                )
                            }

                            Text(
                                text = "$waterGlasses / ${userProfile.dailyWaterGlassesTarget} glasses (${waterGlasses * 250} ml)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WaterColor
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 1..8) {
                                val isFilled = i <= waterGlasses
                                Box(
                                    modifier = Modifier
                                        .size(width = 18.dp, height = 28.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isFilled) WaterColor else Color(0xFFE5E5EA))
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(NutracalWhite)
                                    .clickable { viewModel.addWater() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("water_add_glass_btn")
                            ) {
                                Text(
                                    text = Localization.t("add_glass", language),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NutracalBlack
                                )
                            }
                        }
                    }
                }
            }

            // Weekly Habit Insight Card
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
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NutracalAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lightbulb,
                                contentDescription = "Insight",
                                tint = NutracalAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = Localization.t("weekly_insight_title", language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val tipText = when (language) {
                                AppLanguage.HINDI -> "भारतीय थाली में दाल और दही का अनुपात रोटी से दोगुना रखने पर भोजन के बाद की सुस्ती 40% कम होती है और इंसुलिन स्पाइक स्थिर रहता है।"
                                AppLanguage.HINGLISH -> "Roti se pehle ek katori salad ya chaas lene se glucose spike 35% tak slow ho jata hai. Perfect for steady afternoon energy!"
                                AppLanguage.ENGLISH -> "Pairing whole wheat rotis with curd or spiced chaas reduces glycemic velocity by up to 35%, supporting sustained metabolic energy."
                            }
                            Text(
                                text = tipText,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = NutracalBlack.copy(alpha = 0.85f)
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
fun EquationColumn(
    label: String,
    value: String,
    highlightColor: Color? = null,
    isBold: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = if (isBold) 17.sp else 15.sp,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Bold,
            color = highlightColor ?: NutracalBlack
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = NutracalSecondary
        )
    }
}

@Composable
fun MealSectionCard(
    mealType: MealType,
    title: String,
    icon: ImageVector,
    meals: List<MealEntity>,
    onAddFood: () -> Unit,
    onDeleteMeal: (MealEntity) -> Unit
) {
    val totalSectionCal = meals.sumOf { it.calories }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("meal_section_${mealType.name.lowercase()}"),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = NutracalBlack,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )
                }

                Text(
                    text = "$totalSectionCal kcal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalBlack
                )
            }

            if (meals.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 0.5.dp, color = NutracalCardBorder)
                Spacer(modifier = Modifier.height(8.dp))

                meals.forEach { meal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = meal.mealTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = NutracalBlack,
                                maxLines = 1
                            )
                            Text(
                                text = "P: ${meal.proteinG.toInt()}g • C: ${meal.carbsG.toInt()}g • F: ${meal.fatG.toInt()}g",
                                fontSize = 11.sp,
                                color = NutracalSecondary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${meal.calories}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            IconButton(
                                onClick = { onDeleteMeal(meal) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = NutracalSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // "+ Add Food" action button for this section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NutracalWhite)
                    .clickable { onAddFood() }
                    .padding(vertical = 8.dp)
                    .testTag("add_food_${mealType.name.lowercase()}"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add",
                        tint = NutracalBlack,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ADD FOOD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
