package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.NutracalBlack
import com.example.ui.theme.NutracalCardBg
import com.example.ui.theme.NutracalSecondary
import com.example.ui.theme.NutracalWhite

@Composable
fun LanguagePill(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        AppLanguage.ENGLISH to "EN",
        AppLanguage.HINDI to "हिंदी",
        AppLanguage.HINGLISH to "Hinglish"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(NutracalCardBg)
            .padding(3.dp)
            .testTag("language_pill_container")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { (lang, label) ->
                val isSelected = currentLanguage == lang
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) NutracalWhite else Color.Transparent,
                    animationSpec = spring(),
                    label = "lang_bg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) NutracalBlack else NutracalSecondary,
                    label = "lang_text"
                )

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .then(
                            if (isSelected) Modifier.shadow(2.dp, RoundedCornerShape(16.dp))
                            else Modifier
                        )
                        .background(bgColor)
                        .clickable { onLanguageSelected(lang) }
                        .padding(horizontal = 10.dp)
                        .testTag("lang_btn_${lang.code}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = textColor
                    )
                }
            }
        }
    }
}
