package com.sportspulse.app.data.repository

import com.sportspulse.app.data.model.Article
import com.sportspulse.app.data.remote.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

sealed interface ArticlesResult {
    data class Success(val articles: List<Article>) : ArticlesResult
    data class Error(val message: String) : ArticlesResult
}

class ArticleRepository(
    private val api: com.sportspulse.app.data.remote.ApiService = NetworkModule.apiService,
) {
    // Cache simplu in memorie: API-ul public nu are endpoint pt un singur articol,
    // doar lista completa. Populat automat dupa fiecare fetch reusit, folosit de
    // ecranul de detaliu ca sa nu faca un request nou daca articolul e deja cunoscut.
    companion object {
        private val cache = mutableMapOf<String, Article>()
        fun getCached(id: String): Article? = cache[id]
    }

    suspend fun getArticles(section: String? = null): ArticlesResult = withContext(Dispatchers.IO) {
        try {
            val articles = api.getArticles(section)
            articles.forEach { cache[it.id] = it }
            ArticlesResult.Success(articles)
        } catch (e: IOException) {
            // Fara conexiune la internet / server nedisponibil
            ArticlesResult.Error("Nu am putut incarca articolele. Verifica conexiunea la internet.")
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                401 -> ArticlesResult.Error("Acces neautorizat la feed.")
                else -> ArticlesResult.Error("A aparut o eroare (${e.code()}).")
            }
        } catch (e: Exception) {
            ArticlesResult.Error("A aparut o eroare neasteptata.")
        }
    }
}
