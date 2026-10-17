package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.CookingStyle
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite

@Composable
fun CookingStylePicker(
    selectedStyle: CookingStyle,
    onStyleSelected: (CookingStyle) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = Localization.t("cooking_style_label", language),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = NutracalBlack
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CookingStyle.values().forEach { style ->
                val isSelected = selectedStyle == style
                val title = when (language) {
                    AppLanguage.HINDI -> style.titleHi
                    AppLanguage.HINGLISH -> style.titleHinglish
                    AppLanguage.ENGLISH -> style.titleEn
                }

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) NutracalBlack else NutracalCardBg,
                    label = "style_bg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) NutracalWhite else NutracalBlack,
                    label = "style_text"
                )
                val subColor by animateColorAsState(
                    targetValue = if (isSelected) NutracalWhite.copy(alpha = 0.75f) else NutracalSecondary,
                    label = "style_sub"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(bgColor)
                        .clickable { onStyleSelected(style) }
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .testTag("cooking_style_${style.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = style.oilNote,
                            fontSize = 10.sp,
                            color = subColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
