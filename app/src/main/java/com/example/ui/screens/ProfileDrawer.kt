package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.FitnessGoal
import com.example.ui.components.LanguagePill
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalRed
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite
import com.example.viewmodel.NutracalViewModel

@Composable
fun ProfileDrawerContent(
    viewModel: NutracalViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    ModalDrawerSheet(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight(),
        drawerContainerColor = NutracalWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .testTag("profile_drawer_sheet"),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile & Goals",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutracalBlack
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = NutracalSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // User Avatar and Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(NutracalBlack),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            tint = NutracalWhite,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = userProfile.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutracalBlack
                        )
                        Text(
                            text = userProfile.email.ifBlank { "aarav@example.com" },
                            fontSize = 12.sp,
                            color = NutracalSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 0.5.dp, color = NutracalCardBorder)
                Spacer(modifier = Modifier.height(16.dp))

                // Stats Grid
                Text(
                    text = "Health Metrics",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = NutracalCardBg)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "BMI", fontSize = 13.sp, color = NutracalSecondary)
                            Text(
                                text = "${userProfile.bmi} (${userProfile.bmiCategory})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Height & Weight", fontSize = 13.sp, color = NutracalSecondary)
                            Text(
                                text = "${userProfile.heightCm.toInt()} cm / ${userProfile.weightKg.toInt()} kg",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NutracalBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Calorie Target", fontSize = 13.sp, color = NutracalSecondary)
                            Text(
                                text = "${userProfile.dailyCalorieTarget} kcal",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Protein Target", fontSize = 13.sp, color = NutracalSecondary)
                            Text(
                                text = "${userProfile.dailyProteinTarget} g",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NutracalGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Goal Selector
                Text(
                    text = "Edit Active Goal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                FitnessGoal.values().forEach { goal ->
                    val isSel = userProfile.goal == goal
                    val title = when (language) {
                        AppLanguage.HINDI -> goal.titleHi
                        AppLanguage.HINGLISH -> goal.titleHinglish
                        AppLanguage.ENGLISH -> goal.titleEn
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSel) NutracalBlack else NutracalCardBg)
                            .clickable {
                                viewModel.completeOnboarding(userProfile.copy(goal = goal))
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) NutracalWhite else NutracalBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Language Selector
                Text(
                    text = "App Language",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutracalSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LanguagePill(
                    currentLanguage = language,
                    onLanguageSelected = { viewModel.setLanguage(it) }
                )
            }

            // Bottom Actions (Reset Data & Logout)
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 0.5.dp, color = NutracalCardBorder)
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.resetWater()
                            onClose()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = null,
                        tint = NutracalSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Reset Water Counter",
                        fontSize = 14.sp,
                        color = NutracalSecondary
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.logout()
                            onClose()
                        }
                        .padding(vertical = 8.dp)
                        .testTag("drawer_logout_btn"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Logout,
                        contentDescription = null,
                        tint = NutracalRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Log Out / Switch Account",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutracalRed
                    )
                }
            }
        }
    }
}
