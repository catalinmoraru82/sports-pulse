package com.sportspulse.app.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sportspulse.app.data.model.Article
import com.sportspulse.app.ui.components.ArticleCard
import com.sportspulse.app.ui.components.ForceStatusBarIcons
import com.sportspulse.app.ui.components.TopBarHeight
import com.sportspulse.app.ui.theme.OnTopBarBlack
import com.sportspulse.app.ui.theme.TopBarBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onArticleClick: (Article) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: FeedViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // Banda de sus e mereu neagra (vezi mai jos) - iconitele barei de sistem trebuie
    // sa ramana deschise la culoare pe acest ecran, indiferent de tema aleasa.
    ForceStatusBarIcons(useLightIcons = true)

    Scaffold(
        topBar = {
            // Banda de sus e mereu neagra (culoare fixa, nu MaterialTheme.colorScheme.surface) -
            // pe light theme verdele nu se vedea bine pe fundal alb, asta il pastreaza lizibil
            // pe ambele teme.
            Surface(color = TopBarBlack) {
                // Titlul centrat pe orizontala (ca in Settings), iconita de setari ramane in dreapta.
                // Box in loc de Row cu SpaceBetween: SpaceBetween ar fi lasat titlul in stanga.
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(TopBarHeight)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "SportsPulse",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Setari",
                            tint = OnTopBarBlack,
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        // Continutul e mereu vizibil sub indicator - refresh-ul nu ascunde lista existenta,
        // doar arata iconita de loading in timp ce se reincarca in fundal.
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            when (val state = uiState) {
                is FeedUiState.Loading -> LoadingState()
                is FeedUiState.Error -> ErrorState(state.message, onRetry = { viewModel.loadArticles() })
                is FeedUiState.Success -> {
                    if (state.articles.isEmpty()) {
                        EmptyState()
                    } else {
                        ArticleList(articles = state.articles, onArticleClick = onArticleClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(articles, key = { it.id }) { article ->
            ArticleCard(
                article = article,
                onClick = { onArticleClick(article) },
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            androidx.compose.foundation.layout.Spacer(Modifier.height(12.dp))
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text("Incearca din nou", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Niciun articol publicat momentan.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
