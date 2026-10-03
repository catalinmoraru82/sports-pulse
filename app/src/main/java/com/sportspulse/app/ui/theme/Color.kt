package com.sportspulse.app.ui.theme

import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.graphics.Color

// ============================================================
// Culori extrase direct din fisierul Figma (frame-urile feed-light / detail-dark).
// Accentul de brand a fost schimbat la cererea ta la verdele extras dintr-un
// fisier Figma nou: #C5F900 (verde-lime energic). Pastram acelasi restul de
// conventii tonale M3 (surface FFFBFE, on-surface 1C1B1F etc.) - doar accentul
// s-a schimbat, structura si paginile raman neatinse.
// ============================================================

// --- Light scheme ---
val AccentGreenLight = Color(0xFFC5F900)
// Verdele e foarte deschis/aprins - text alb pe el ar avea contrast prost.
// Folosim un ton inchis (acelasi gri-inchis-albastrui vazut si in designul
// din Figma la butoanele rotunde, #121418) pt lizibilitate buna.
val OnPrimaryLight = Color(0xFF121418)
// Pagina in sine - alb curat (nu aproape-alb cu nuanta subtila, care putea sa para
// un gradient la fotografiere pe unele ecrane).
val SurfaceLight = Color(0xFFFFFFFF)
// Cardurile - gri deschis, clar distinct de fundalul alb al paginii (nu doar
// o nuanta aproape imperceptibila) - la cererea ta, un "flat light gray" clar.
val SurfaceContainerLight = Color(0xFFF2F2F5)
// Ton pt cardurile "deja vizitate" - vizibil mai gri decat cardul normal de mai sus.
val SurfaceContainerHighLight = Color(0xFFE5E5EA)
val OnSurfaceLight = Color(0xFF1C1B1F)
val OnSurfaceVariantLight = Color(0xFF000000)
val OutlineVariantLight = Color(0xFFE7E0EC)
val ErrorLight = Color(0xFFBA1A1A)

// --- Dark scheme ---
// Negrii reali extrasi din fisierul Figma (design-ul de player), nu mai sunt
// tonuri M3 generice derivate: 090A0C fundal principal, 121418 carduri/butoane,
// 1F232B contur/border, 1A1D24 (bara de progres in Figma) pt "vizitat".
val AccentGreenDark = Color(0xFFC5F900)
val OnPrimaryDark = Color(0xFF121418)
val SurfaceDark = Color(0xFF090A0C)
val SurfaceContainerDark = Color(0xFF121418)
// Ton pt cardurile "deja vizitate" pe dark - preluat direct din Figma (culoarea
// barei de progres neincarcate, #1A1D24), usor mai deschis decat containerul normal.
val SurfaceContainerHighDark = Color(0xFF1A1D24)
val OnSurfaceDark = Color(0xFFE6E1E5)
val OnSurfaceVariantDark = Color(0xFFFFFFFF)
//val OnSurfaceVariantDark = Color(0xFF8E939E)
val OutlineVariantDark = Color(0xFF1F232B)
val ErrorDark = Color(0xFFFFB4AB)

// --- Banda de sus (top bar) cu logo-ul "SportsPulse" ---
// La cererea ta: ramane neagra (culoarea reala de fundal din Figma) + logo verde,
// pe AMBELE teme - nu doar pe dark. Pe light theme verdele nu se vedea bine pe
// fundal alb, asta rezolva exact problema. E o culoare fixa, separata de tema
// (nu MaterialTheme.colorScheme.surface), folosita explicit in FeedScreen si
// ArticleWebViewScreen.
val TopBarBlack = Color(0xFF090A0C)
// Text/iconite pe banda neagra - mereu deschise la culoare, indiferent de tema.
val OnTopBarBlack = Color(0xFFE6E1E5)