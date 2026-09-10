package com.sportspulse.app.ui.navigation

import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object Feed : Screen("feed")
    data object Settings : Screen("settings")

    // Deschide articolul original in WebView, in interiorul aplicatiei - nu mai
    // avem ecran custom de detaliu. URL-ul e codificat ca sa poata trece prin ruta
    // (poate contine "/" si alte caractere speciale).
    data object ArticleWebView : Screen("webview/{articleId}/{encodedUrl}") {
        fun createRoute(articleId: String, url: String): String {
            val encoded = URLEncoder.encode(url, "UTF-8")
            return "webview/$articleId/$encoded"
        }

        fun decodeUrl(encodedUrl: String): String = URLDecoder.decode(encodedUrl, "UTF-8")
    }
}
