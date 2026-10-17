package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBorder
import com.example.ui.theme.NutracalGreen
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite

@Composable
fun BottomTabBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NutracalWhite)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(thickness = 0.5.dp, color = NutracalCardBorder)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Diary (MyFitnessPal style)
                TabItem(
                    label = Localization.t("tab_home", language),
                    icon = Icons.Outlined.DateRange,
                    isSelected = selectedTab == "home",
                    onClick = { onTabSelected("home") },
                    testTag = "nav_tab_diary"
                )

                // Tab 2: Search (Food database & barcode)
                TabItem(
                    label = Localization.t("tab_search", language),
                    icon = Icons.Outlined.Search,
                    isSelected = selectedTab == "search",
                    onClick = { onTabSelected("search") },
                    testTag = "nav_tab_search"
                )

                // Spacer for center elevated Scan button
                Spacer(modifier = Modifier.size(54.dp))

                // Tab 4: Progress (Weight & nutrient insights)
                TabItem(
                    label = Localization.t("tab_insights", language),
                    icon = Icons.AutoMirrored.Outlined.TrendingUp,
                    isSelected = selectedTab == "insights",
                    onClick = { onTabSelected("insights") },
                    testTag = "nav_tab_progress"
                )

                // Tab 5: Settings (MyFitnessPal goals, macros & preferences)
                TabItem(
                    label = Localization.t("tab_settings", language),
                    icon = Icons.Outlined.Settings,
                    isSelected = selectedTab == "settings",
                    onClick = { onTabSelected("settings") },
                    testTag = "nav_tab_settings"
                )
            }

            // Large center circular Scan button (Cal AI iconic scanner)
            CenterScanButton(
                isSelected = selectedTab == "scan",
                onClick = { onTabSelected("scan") },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-14).dp)
            )
        }
    }
}

@Composable
fun CenterScanButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = if (isPressed) 0.92f else 1.0f

    Box(
        modifier = modifier
            .scale(scale)
            .size(62.dp)
            .shadow(8.dp, CircleShape, spotColor = NutracalBlack.copy(alpha = 0.35f))
            .clip(CircleShape)
            .background(NutracalBlack)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag("nav_center_scan_btn"),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(NutracalBlack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.CenterFocusStrong,
                contentDescription = "Scan AI",
                tint = if (isSelected) NutracalGreen else NutracalWhite,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = if (isPressed) 0.92f else 1.0f

    val color by animateColorAsState(
        targetValue = if (isSelected) NutracalBlack else NutracalSecondary,
        label = "tab_color"
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = color
        )
    }
}
