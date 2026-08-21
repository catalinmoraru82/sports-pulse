package com.sportspulse.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Campurile corespund exact raspunsului de la GET /api/public/articles din admin
// (vezi app/api/public/articles/route.ts din proiectul sports-admin).
@Serializable
data class Article(
    val id: String,
    @SerialName("source_id") val sourceId: String? = null,
    @SerialName("source_name") val sourceName: String? = null,
    @SerialName("is_manual") val isManual: Boolean = false,
    val title: String,
    val summary: String? = null,
    val content: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("original_url") val originalUrl: String? = null,
    val author: String? = null,
    val section: String? = null,
    val status: String,
    @SerialName("is_highlighted") val isHighlighted: Boolean = false,
    val position: Int? = null,
    @SerialName("published_at") val publishedAt: String? = null,
)
