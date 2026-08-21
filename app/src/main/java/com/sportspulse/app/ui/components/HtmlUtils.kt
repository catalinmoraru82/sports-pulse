package com.sportspulse.app.ui.components

import android.text.Html

/**
 * Multe surse RSS trimit summary/content ca HTML brut (ex: "<p>Alexandru Musi a devenit...</p>").
 * Html.fromHtml scoate tag-urile SI decodeaza entitatile (&amp;, &nbsp; etc.), spre deosebire
 * de un simplu regex care ar lasa entitatile neschimbate.
 */
fun stripHtml(input: String?): String? {
    if (input.isNullOrBlank()) return input
    return Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT)
        .toString()
        .trim()
        .replace(Regex("\\n{2,}"), "\n") // paragrafele multiple din <p> nu lasa spatii goale exagerate
}
