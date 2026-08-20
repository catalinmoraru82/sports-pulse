package com.sportspulse.app.ui.components

import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Transforma un timestamp ISO-8601 (asa cum vine din API, ex: "2026-08-15T10:30:00Z")
 * in text relativ, la fel ca in design ("2 hours ago", "1 day ago").
 * minSdk 31 => java.time e disponibil nativ, fara nevoie de desugaring.
 */
fun relativeTime(isoTimestamp: String): String {
    val instant = try {
        Instant.parse(isoTimestamp)
    } catch (e: Exception) {
        return ""
    }

    val now = Instant.now()
    val minutes = ChronoUnit.MINUTES.between(instant, now)
    val hours = ChronoUnit.HOURS.between(instant, now)
    val days = ChronoUnit.DAYS.between(instant, now)

    return when {
        minutes < 1 -> "chiar acum"
        minutes < 60 -> "acum $minutes min"
        hours < 24 -> "acum $hours ${if (hours == 1L) "ora" else "ore"}"
        days < 7 -> "acum $days ${if (days == 1L) "zi" else "zile"}"
        else -> "acum ${days / 7} ${if (days / 7 == 1L) "saptamana" else "saptamani"}"
    }
}
