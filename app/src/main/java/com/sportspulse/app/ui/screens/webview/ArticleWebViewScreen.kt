package com.sportspulse.app.ui.screens.webview

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sportspulse.app.R
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
                            contentDescription = stringResource(R.string.action_back),
                            tint = OnTopBarBlack,
                        )
                    }
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    IconButton(
                        onClick = {
                            // Link-ul articolului + semnatura aplicatiei dedesubt. Link-ul Play Store se
                            // construieste din packageName, deci ramane corect orice s-ar schimba.
                            val playStoreUrl = "https://play.google.com/store/apps/details?id=${context.packageName}"
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text, url, playStoreUrl))
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        },
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.action_share), tint = OnTopBarBlack)
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
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest,
                            ): Boolean = handleUrl(view, request.url)

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
                // Apelat cand ecranul iese din compozitie (back / navigare in alta parte): oprim
                // incarcarea, scoatem callback-urile si eliberam WebView-ul (altfel ramane in
                // memorie, cu JavaScript si conexiuni active, pana la GC).
                onRelease = { view ->
                    webView = null
                    view.stopLoading()
                    view.webViewClient = WebViewClient()
                    view.webChromeClient = null
                    view.destroy()
                },
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

/**
 * Decide ce se intampla cand pagina din WebView incearca sa navigheze la [uri].
 * Returneaza true = am tratat noi navigarea (WebView-ul nu o mai incarca), false = o incarca el.
 *
 * - http/https: ramane in WebView, comportament normal.
 * - intent:  linkuri "intent://..." (ex: deschide aplicatia sursei). Se deschide aplicatia daca e
 *            instalata; altfel se foloseste browser_fallback_url, daca pagina a trimis unul.
 * - mailto:/tel:/sms:/geo:/market: etc.: se paseaza aplicatiei potrivite din telefon.
 * - file:/content:: blocate. Orice alta schema necunoscuta: se incearca deschiderea cu o aplicatie din
 *   telefon; daca nu exista niciuna, nu se intampla nimic (nu apare ERR_UNKNOWN_URL_SCHEME).
 */
private fun handleUrl(view: WebView, uri: Uri): Boolean {
    return when (uri.scheme?.lowercase()) {
        "http", "https" -> false
        // about:blank, data:, javascript: si blob: sunt folosite intern de pagini - le lasam in pace.
        "about", "data", "javascript", "blob" -> false
        // Acces la fisiere/continut local din aplicatie: o pagina web nu are ce cauta aici - blocat.
        "file", "content" -> true
        "intent" -> {
            openIntentUrl(view, uri.toString())
            true
        }
        "mailto" -> {
            startExternal(view.context, Intent(Intent.ACTION_SENDTO, uri))
            true
        }
        else -> {
            startExternal(view.context, Intent(Intent.ACTION_VIEW, uri))
            true
        }
    }
}

private fun openIntentUrl(view: WebView, url: String) {
    val intent = try {
        Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
    } catch (_: Exception) {
        return // URL "intent:" malformat - il ignoram
    }

    // Masuri de siguranta standard: o pagina web nu trebuie sa poata tinti o componenta
    // anume din aplicatia noastra sau din alta aplicatie, ci doar sa deschida ceva "browsable".
    intent.addCategory(Intent.CATEGORY_BROWSABLE)
    intent.component = null
    intent.selector = null

    if (startExternal(view.context, intent)) return

    // Aplicatia nu e instalata: folosim fallback-ul oferit de pagina, daca exista si e web.
    val fallback = intent.getStringExtra("browser_fallback_url")
    if (fallback != null) {
        val fallbackUri = Uri.parse(fallback)
        if (fallbackUri.scheme == "http" || fallbackUri.scheme == "https") {
            view.loadUrl(fallback)
        }
    }
}

// true daca s-a gasit o aplicatie care sa deschida intentul, false daca nu exista niciuna.
private fun startExternal(context: Context, intent: Intent): Boolean {
    return try {
        context.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }
}
