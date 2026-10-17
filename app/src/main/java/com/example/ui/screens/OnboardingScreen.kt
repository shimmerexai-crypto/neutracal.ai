package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.FitnessGoal
import com.example.model.Gender
import com.example.model.UserProfile
import com.example.ui.components.LanguagePill
import com.example.ui.theme.NutracalAmber
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalRed
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite

@Composable
fun OnboardingScreen(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onComplete: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) } // 1 to 6
    val totalSteps = 6

    var name by remember { mutableStateOf("Aarav Sharma") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var age by remember { mutableIntStateOf(26) }
    var heightCm by remember { mutableFloatStateOf(172f) }
    var weightKg by remember { mutableFloatStateOf(70f) }
    var source by remember { mutableStateOf("Instagram") }
    var goal by remember { mutableStateOf(FitnessGoal.WEIGHT_LOSS) }

    // BMI calculation
    val heightM = heightCm / 100f
    val bmi = if (heightM > 0) ((weightKg / (heightM * heightM)) * 10).toInt() / 10f else 22.0f
    val bmiCategory = when {
        bmi < 18.5f -> "Underweight"
        bmi < 24.9f -> "Normal (Healthy)"
        bmi < 29.9f -> "Overweight"
        else -> "Obese"
    }

    // Daily Targets Calculation based on Mifflin-St Jeor + Indian dietary guidelines
    val bmr = when (gender) {
        Gender.MALE -> 10 * weightKg + 6.25f * heightCm - 5 * age + 5
        Gender.FEMALE -> 10 * weightKg + 6.25f * heightCm - 5 * age - 161
        Gender.OTHER -> 10 * weightKg + 6.25f * heightCm - 5 * age - 78
    }

    val maintenanceCal = (bmr * 1.35f).toInt()
    val dailyCalories = when (goal) {
        FitnessGoal.WEIGHT_LOSS -> (maintenanceCal - 400).coerceAtLeast(1400)
        FitnessGoal.MUSCLE_GAIN -> maintenanceCal + 300
        FitnessGoal.MAINTAIN_SUGAR -> maintenanceCal
    }

    val dailyProtein = when (goal) {
        FitnessGoal.WEIGHT_LOSS -> (weightKg * 1.5f).toInt()
        FitnessGoal.MUSCLE_GAIN -> (weightKg * 1.8f).toInt()
        FitnessGoal.MAINTAIN_SUGAR -> (weightKg * 1.2f).toInt()
    }

    val dailyFat = (dailyCalories * 0.25f / 9f).toInt()
    val dailyCarbs = ((dailyCalories - (dailyProtein * 4) - (dailyFat * 9)) / 4f).toInt()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutracalWhite)
            .statusBarsPadding()
            .padding(24.dp)
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Progress & Language
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step > 1) {
                        IconButton(
                            onClick = { step -= 1 },
                            modifier = Modifier.testTag("onboarding_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back",
                                tint = NutracalBlack
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(40.dp))
                    }

                    Text(
                        text = "$step of $totalSteps",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalSecondary
                    )

                    LanguagePill(
                        currentLanguage = currentLanguage,
                        onLanguageSelected = onLanguageSelected
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { step.toFloat() / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NutracalBlack,
                    trackColor = NutracalCardBg
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step Content
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "onboarding_step"
            ) { currentStep ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top
                ) {
                    when (currentStep) {
                        1 -> {
                            // Step 1: Name
                            Text(
                                text = Localization.t("step_name", currentLanguage),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "We'll tailor your daily Indian meals to you.",
                                fontSize = 14.sp,
                                color = NutracalSecondary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("onboarding_name_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NutracalBlack,
                                    unfocusedBorderColor = NutracalCardBorder,
                                    focusedContainerColor = NutracalCardBg,
                                    unfocusedContainerColor = NutracalCardBg
                                )
                            )
                        }
                        2 -> {
                            // Step 2: Gender
                            Text(
                                text = Localization.t("step_gender", currentLanguage),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Used to calibrate basal metabolic rate (BMR).",
                                fontSize = 14.sp,
                                color = NutracalSecondary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Gender.values().forEach { g ->
                                val isSel = gender == g
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable { gender = g }
                                        .testTag("gender_option_${g.name.lowercase()}"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSel) NutracalBlack else NutracalCardBg
                                    ),
                                    border = if (!isSel) androidx.compose.foundation.BorderStroke(1.dp, NutracalCardBorder) else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = g.displayName,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSel) NutracalWhite else NutracalBlack
                                        )
                                        if (isSel) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = null,
                                                tint = NutracalGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        3 -> {
                            // Step 3: Age
                            Text(
                                text = Localization.t("step_age", currentLanguage),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Age helps determine glycemic insulin sensitivity.",
                                fontSize = 14.sp,
                                color = NutracalSecondary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedTextField(
                                value = age.toString(),
                                onValueChange = { age = it.toIntOrNull() ?: age },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                suffix = { Text("years old") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("onboarding_age_input"),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NutracalBlack,
                                    unfocusedBorderColor = NutracalCardBorder,
                                    focusedContainerColor = NutracalCardBg,
                                    unfocusedContainerColor = NutracalCardBg
                                )
                            )
                        }
                        4 -> {
                            // Step 4: Height & Weight (BMI Calculation)
                            Text(
                                text = Localization.t("step_body", currentLanguage),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Real-time BMI calculation according to ICMR guidelines.",
                                fontSize = 14.sp,
                                color = NutracalSecondary
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = heightCm.toInt().toString(),
                                    onValueChange = { heightCm = it.toFloatOrNull() ?: heightCm },
                                    label = { Text("Height (cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("onboarding_height_input"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NutracalBlack,
                                        unfocusedBorderColor = NutracalCardBorder,
                                        focusedContainerColor = NutracalCardBg,
                                        unfocusedContainerColor = NutracalCardBg
                                    )
                                )
                                OutlinedTextField(
                                    value = weightKg.toInt().toString(),
                                    onValueChange = { weightKg = it.toFloatOrNull() ?: weightKg },
                                    label = { Text("Weight (kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("onboarding_weight_input"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NutracalBlack,
                                        unfocusedBorderColor = NutracalCardBorder,
                                        focusedContainerColor = NutracalCardBg,
                                        unfocusedContainerColor = NutracalCardBg
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // BMI Result Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Your BMI",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = NutracalSecondary
                                        )
                                        Text(
                                            text = "$bmi",
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NutracalBlack
                                        )
                                    }

                                    val badgeColor = when {
                                        bmi < 18.5f -> NutracalAmber
                                        bmi < 24.9f -> NutracalGreen
                                        bmi < 29.9f -> NutracalAmber
                                        else -> NutracalRed
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(badgeColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = bmiCategory,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = badgeColor
                                        )
                                    }
                                }
                            }
                        }
                        5 -> {
                            // Step 5: Referral Source
                            Text(
                                text = Localization.t("step_referral", currentLanguage),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            val sources = listOf("Instagram", "Friend", "Google", "YouTube")
                            sources.forEach { s ->
                                val isSel = source == s
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable { source = s }
                                        .testTag("referral_source_${s.lowercase()}"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSel) NutracalBlack else NutracalCardBg
                                    ),
                                    border = if (!isSel) androidx.compose.foundation.BorderStroke(1.dp, NutracalCardBorder) else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = s,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSel) NutracalWhite else NutracalBlack
                                        )
                                        if (isSel) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = null,
                                                tint = NutracalGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        6 -> {
                            // Step 6: Goal Selection & Daily Targets calculation
                            Text(
                                text = Localization.t("step_goal", currentLanguage),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            FitnessGoal.values().forEach { g ->
                                val isSel = goal == g
                                val title = when (currentLanguage) {
                                    AppLanguage.HINDI -> g.titleHi
                                    AppLanguage.HINGLISH -> g.titleHinglish
                                    AppLanguage.ENGLISH -> g.titleEn
                                }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .clickable { goal = g }
                                        .testTag("goal_option_${g.name.lowercase()}"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSel) NutracalBlack else NutracalCardBg
                                    ),
                                    border = if (!isSel) androidx.compose.foundation.BorderStroke(1.dp, NutracalCardBorder) else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSel) NutracalWhite else NutracalBlack
                                        )
                                        if (isSel) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = null,
                                                tint = NutracalGreen
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Computed Targets Preview Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Calculated Daily Targets",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NutracalSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = "Calories", fontSize = 11.sp, color = NutracalSecondary)
                                            Text(text = "$dailyCalories kcal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                                        }
                                        Column {
                                            Text(text = "Protein", fontSize = 11.sp, color = NutracalSecondary)
                                            Text(text = "${dailyProtein}g", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalGreen)
                                        }
                                        Column {
                                            Text(text = "Carbs", fontSize = 11.sp, color = NutracalSecondary)
                                            Text(text = "${dailyCarbs}g", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalAmber)
                                        }
                                        Column {
                                            Text(text = "Fat", fontSize = 11.sp, color = NutracalSecondary)
                                            Text(text = "${dailyFat}g", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NutracalBlack)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Continue / Finish Button
            Button(
                onClick = {
                    if (step < totalSteps) {
                        step += 1
                    } else {
                        val profile = UserProfile(
                            name = name,
                            gender = gender,
                            age = age,
                            heightCm = heightCm,
                            weightKg = weightKg,
                            source = source,
                            goal = goal,
                            dailyCalorieTarget = dailyCalories,
                            dailyProteinTarget = dailyProtein,
                            dailyCarbsTarget = dailyCarbs,
                            dailyFatTarget = dailyFat,
                            dailyWaterGlassesTarget = 10
                        )
                        onComplete(profile)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("onboarding_next_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NutracalBlack)
            ) {
                Text(
                    text = if (step < totalSteps) Localization.t("continue_btn", currentLanguage)
                           else Localization.t("finish_setup", currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalWhite
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = NutracalWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
