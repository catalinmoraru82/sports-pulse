package com.sportspulse.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Figma foloseste fontul "Inter" in mai multe greutati (Regular/Medium/Bold/ExtraBold).
// FontFamily.Default (Roboto) e folosit ca fallback pana adaugi fisierele .ttf de Inter
// in res/font/ - vezi nota de mai jos.
//
// CA SA FOLOSESTI CHIAR FONTUL INTER (recomandat, ca sa arate identic cu Figma):
// 1. Descarca Inter de pe https://fonts.google.com/specimen/Inter
// 2. Pune fisierele .ttf in app/src/main/res/font/ (inter_regular.ttf, inter_medium.ttf,
//    inter_bold.ttf, inter_extrabold.ttf)
// 3. Inlocuieste FontFamily.Default de mai jos cu:
//    FontFamily(Font(R.font.inter_regular, FontWeight.Normal), Font(R.font.inter_medium, FontWeight.Medium), ...)
val InterFontFamily = FontFamily.Default

val AppTypography = Typography(
    // Titlu brand ("SportsPulse" in top app bar) - 20px ExtraBold in Figma
    headlineSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    // Titlu articol featured - 18px ExtraBold
    titleLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    ),
    // Titlu articol normal - 16px Bold
    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 21.sp,
    ),
    // Corp text (rezumat articol featured) - 14px Regular
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    // Corp text mic (rezumat articol normal) - 13px Regular
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    // Meta info (sursa, timp) - 12px
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    // Badge "Featured" - 11px ExtraBold uppercase
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
)
