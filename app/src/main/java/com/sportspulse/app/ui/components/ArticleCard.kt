package com.sportspulse.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sportspulse.app.data.model.Article

/**
 * Card articol - replica designul din Figma. Are doua variante:
 * - featured (is_highlighted = true): badge "FEATURED" portocaliu, titlu mai mare
 * - normal: fara badge, titlu mai mic
 */
@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (!article.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 12.dp),
                )
            }

            if (article.isHighlighted) {
                FeaturedBadge()
                androidx.compose.foundation.layout.Spacer(Modifier.height(10.dp))
            }

            Text(
                text = article.title,
                style = if (article.isHighlighted) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            if (!article.summary.isNullOrBlank()) {
                androidx.compose.foundation.layout.Spacer(Modifier.height(10.dp))
                Text(
                    text = article.summary,
                    style = if (article.isHighlighted) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            androidx.compose.foundation.layout.Spacer(Modifier.height(10.dp))

            ArticleMeta(
                source = article.section?.replaceFirstChar { it.uppercase() },
                publishedAt = article.publishedAt,
            )
        }
    }
}

@Composable
private fun FeaturedBadge() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = "FEATURED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun ArticleMeta(source: String?, publishedAt: String?) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (!source.isNullOrBlank()) {
            Text(
                text = source,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!source.isNullOrBlank() && !publishedAt.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant, CircleShape),
            )
        }
        if (!publishedAt.isNullOrBlank()) {
            Text(
                text = relativeTime(publishedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
