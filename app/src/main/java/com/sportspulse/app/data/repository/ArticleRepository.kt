package com.sportspulse.app.data.repository

import androidx.annotation.StringRes
import com.sportspulse.app.R
import com.sportspulse.app.data.model.Article
import com.sportspulse.app.data.remote.NetworkModule
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

sealed interface ArticlesPageResult {
    data class Success(val articles: List<Article>, val hasMore: Boolean) : ArticlesPageResult
    // messageRes = text afisabil in UI (resursa din strings.xml); httpCode e folosit doar de
    // mesajul generic pentru erori HTTP, care il include in text.
    data class Error(@StringRes val messageRes: Int, val httpCode: Int? = null) : ArticlesPageResult
}

class ArticleRepository(
    private val api: com.sportspulse.app.data.remote.ApiService = NetworkModule.apiService,
) {
    // O singura pagina de articole (page incepe de la 1). Feed-ul incarca pagini pe masura
    // ce userul deruleaza, nu toate articolele odata.
    suspend fun getArticlesPage(
        page: Int,
        pageSize: Int,
        section: String? = null,
    ): ArticlesPageResult = withContext(Dispatchers.IO) {
        try {
            val result = api.getArticlesPage(page, pageSize, section)
            ArticlesPageResult.Success(result.articles, result.hasMore)
        } catch (e: CancellationException) {
            // Cererea a fost anulata intentionat (ex: pull-to-refresh in timpul incarcarii
            // unui lot) - nu e o eroare de retea, trebuie propagata, nu transformata in mesaj.
            throw e
        } catch (e: IOException) {
            // Fara conexiune la internet / server nedisponibil
            ArticlesPageResult.Error(R.string.error_no_connection)
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                401 -> ArticlesPageResult.Error(R.string.error_unauthorized)
                else -> ArticlesPageResult.Error(R.string.error_http, e.code())
            }
        } catch (e: Exception) {
            ArticlesPageResult.Error(R.string.error_unknown)
        }
    }

    // Trimite un "view" catre admin cand userul deschide un articol. Esuat silentios -
    // nu vrem ca o problema de retea la tracking sa afecteze experienta de citit.
    suspend fun markViewed(articleId: String) {
        withContext(Dispatchers.IO) {
            try {
                api.markViewed(articleId)
            } catch (_: Exception) {
                // ignorat intentionat
            }
        }
    }
}
