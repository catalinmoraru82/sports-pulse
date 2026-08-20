package com.sportspulse.app.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// Culori extrase direct din fisierul Figma (frame-urile feed-light / detail-dark).
// Seed-ul de brand e portocaliul FF6D00, restul urmeaza deja convențiile
// tonale Material 3 (surface FFFBFE, on-surface 1C1B1F etc.) - designerul
// a construit deja pe baza M3, ceea ce simplifica mult maparea aici.
// ============================================================

// --- Light scheme ---
val OrangePrimaryLight = Color(0xFFFF6D00)
val OnPrimaryLight = Color(0xFFFFFFFF)
val SurfaceLight = Color(0xFFFFFBFE)
val SurfaceContainerLight = Color(0xFFFFFCFF)
val OnSurfaceLight = Color(0xFF1C1B1F)
val OnSurfaceVariantLight = Color(0xFF5F6368)
val OutlineVariantLight = Color(0xFFE7E0EC)
val ErrorLight = Color(0xFFBA1A1A)

// --- Dark scheme ---
// Nu am valorile exacte hex pt dark din Figma (nu au fost inca extrase toate
// frame-urile), asa ca aplic conventia standard M3 de a deriva dark scheme
// dintr-un seed color - se pot inlocui usor cu valorile exacte cand designul
// e complet.
val OrangePrimaryDark = Color(0xFFFFB77C)
val OnPrimaryDark = Color(0xFF5A2E00)
val SurfaceDark = Color(0xFF1C1B1F)
val SurfaceContainerDark = Color(0xFF211F23)
val OnSurfaceDark = Color(0xFFE6E1E5)
val OnSurfaceVariantDark = Color(0xFFC9C5CA)
val OutlineVariantDark = Color(0xFF49454F)
val ErrorDark = Color(0xFFFFB4AB)
