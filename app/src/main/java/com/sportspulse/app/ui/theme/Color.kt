package com.sportspulse.app.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// Culori extrase direct din fisierul Figma (frame-urile feed-light / detail-dark).
// Seed-ul de brand e portocaliul FF6719, restul urmeaza deja convențiile
// tonale Material 3 (surface FFFBFE, on-surface 1C1B1F etc.) - designerul
// a construit deja pe baza M3, ceea ce simplifica mult maparea aici.
// ============================================================

// --- Light scheme ---
val OrangePrimaryLight = Color(0xFFFF6719)
val OnPrimaryLight = Color(0xFFFFFFFF)
val SurfaceLight = Color(0xFFFFFBFE)
val SurfaceContainerLight = Color(0xFFFFFCFF)
// Ton distinct pt cardurile "deja vizitate" - un gri deschis, vizibil diferit de fundalul normal
// aproape-alb de mai sus (altfel cele doua stari ar arata identic, cum era inainte).
val SurfaceContainerHighLight = Color(0xFFECECEF)
val OnSurfaceLight = Color(0xFF1C1B1F)
val OnSurfaceVariantLight = Color(0xFF5F6368)
val OutlineVariantLight = Color(0xFFE7E0EC)
val ErrorLight = Color(0xFFBA1A1A)

// --- Dark scheme ---
// La cererea ta: aceeasi nuanta exacta ca in light theme, nu o varianta mai deschisa.
val OrangePrimaryDark = Color(0xFFFF6719)
// OnPrimaryDark trecut pe alb (la fel ca OnPrimaryLight) - cu portocaliul saturat FF6719
// ca fundal, un maro inchis (cum era inainte, gandit pt varianta pastel) ar avea contrast slab.
val OnPrimaryDark = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1C1B1F)
val SurfaceContainerDark = Color(0xFF211F23)
// La fel, ton distinct pt "vizitat" pe dark theme - putin mai deschis decat containerul normal.
val SurfaceContainerHighDark = Color(0xFF2C2A30)
val OnSurfaceDark = Color(0xFFE6E1E5)
val OnSurfaceVariantDark = Color(0xFFC9C5CA)
val OutlineVariantDark = Color(0xFF49454F)
val ErrorDark = Color(0xFFFFB4AB)
