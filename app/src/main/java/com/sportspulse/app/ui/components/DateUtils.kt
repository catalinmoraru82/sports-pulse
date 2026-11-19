package com.sportspulse.app.ui.components

import android.content.res.Resources
import com.sportspulse.app.R
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Transforma un timestamp ISO-8601 (asa cum vine din API, ex: "2026-08-15T10:30:00Z")
 * in text relativ ("acum 2 ore", "acum 1 zi"). Textele sunt in strings.xml (plurals), ca sa
 * respecte regulile limbii - in romana: "acum 1 oră", "acum 5 ore", "acum 20 de ore".
 * minSdk 31 => java.time e disponibil nativ, fara nevoie de desugaring.
 */
fun relativeTime(resources: Resources, isoTimestamp: String): String {
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
        minutes < 1 -> resources.getString(R.string.time_just_now)
        minutes < 60 -> resources.getString(R.string.time_minutes_ago, minutes)
        hours < 24 -> resources.getQuantityString(R.plurals.time_hours_ago, hours.toInt(), hours)
        days < 7 -> resources.getQuantityString(R.plurals.time_days_ago, days.toInt(), days)
        else -> {
            val weeks = days / 7
            resources.getQuantityString(R.plurals.time_weeks_ago, weeks.toInt(), weeks)
        }
    }
}
