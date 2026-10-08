package com.sportspulse.app.ui.screens.webview

import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sportspulse.app.data.local.VisitedArticlesStore
import com.sportspulse.app.data.repository.ArticleRepository
import com.sportspulse.app.ui.components.ForceStatusBarIcons
import com.sportspulse.app.ui.components.TopBarHeight
import com.sportspulse.app.ui.theme.OnTopBarBlack
import com.sportspulse.app.ui.theme.TopBarBlack

/**
 * Deschide articolul original al sursei direct in aplicatie, intr-un WebView - nu mai
 * exista un ecran custom de "detaliu articol" cu continut nostru propriu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleWebViewScreen(
    articleId: String,
    url: String,
    onBack: () -> Unit,
    repository: ArticleRepository = remember { ArticleRepository() },
) {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0f) }
    var isLoading by remember { mutableStateOf(true) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(articleId) {
        // Marcam vizitat imediat ce userul intra pe pagina, la fel ca inainte.
        VisitedArticlesStore.markVisited(context, articleId)
        // Inregistram si un "view" in admin - functia interna esueaza silentios
        // daca nu merge reteaua, nu blocheaza nimic din UI.
        repository.markViewed(articleId)
    }

    // Banda de sus e mereu neagra (vezi mai jos) - iconitele barei de sistem trebuie
    // sa ramana deschise la culoare, indiferent de tema.
    ForceStatusBarIcons(useLightIcons = true)

    // Butonul "inapoi" (hardware/gest) navigheaza prima data in istoricul WebView-ului,
    // ca intr-un browser normal - doar cand nu mai are unde sa navigheze inapoi in
    // pagina, inchide efectiv ecranul.
    BackHandler(enabled = webView?.canGoBack() == true) {
        webView?.goBack()
    }

    Scaffold(
        topBar = {
            // Bara custom, aceeasi structura ca in FeedScreen: titlul centrat, iconitele pe
            // margini (back stanga, share dreapta), inaltime fixa TopBarHeight (64dp).
            Surface(color = TopBarBlack) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(TopBarHeight)
                        .padding(horizontal = 4.dp),
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Inapoi",
                            tint = OnTopBarBlack,
                        )
                    }
                    Text(
                        text = "SportsPulse",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    IconButton(
                        onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, url)
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        },
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Trimite", tint = OnTopBarBlack)
                    }
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, finishedUrl: String?) {
                                isLoading = false
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                progress = newProgress / 100f
                                if (newProgress >= 100) isLoading = false
                            }
                        }
                        loadUrl(url)
                        webView = this
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )

            if (isLoading) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    color = MaterialTheme.colorScheme.primary,
                )
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
