package com.sportspulse.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sportspulse.app.R

// Un singur font in toata aplicatia: Outfit (extras din fisierul Figma), la cererea ta.
// E font variabil (un singur .ttf acopera toate greutatile). Setam explicit axa de
// variatie 'wght' pt fiecare instanta (FontVariation.weight) - doar declararea
// parametrului "weight" la Font() nu garanteaza intotdeauna interpolarea corecta
// pe toate device-urile, testele au aratat text randat mereu la aceeasi greutate
// implicita indiferent ce era declarat. Asa e garantat sa foloseasca greutatea corecta.
//
// FontVariation e inca marcata experimentala de Compose (poate sa se schimbe in
// versiuni viitoare de bbiblioteca), dar functioneaza normal - @OptIn confirma ca
// acceptam asta constient.
//
// Licenta OFL e in /licenses la radacina proiectului.
@OptIn(ExperimentalTextApi::class)
val OutfitFontFamily = FontFamily(
    Font(R.font.outfit, weight = FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.outfit, weight = FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.outfit, weight = FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.outfit, weight = FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
    Font(R.font.outfit, weight = FontWeight.ExtraBold, variationSettings = FontVariation.Settings(FontVariation.weight(800))),
)

val AppTypography = Typography(
    // Titlu brand ("SportsPulse" in top app bar) - ExtraBold
    headlineSmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    // Titlu articol featured - ExtraBold (crescut de la Bold, era greu de citit)
    titleLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 23.sp,
    ),
    // Titlu articol normal - Bold (crescut de la SemiBold, era greu de citit)
    titleMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 23.sp,
    ),
    // Corp text (rezumat articol featured) - SemiBold (crescut de la Medium)
    bodyLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 23.sp,
    ),
    // Corp text mic (rezumat articol normal) - SemiBold
    bodyMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 23.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    // Meta info (sursa, timp) - SemiBold->Bold, era prea subtire la citit
    labelMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    // Badge "Featured"
    labelLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
)