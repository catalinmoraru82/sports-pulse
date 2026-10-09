package com.sportspulse.app.data.remote

import com.sportspulse.app.data.model.Article
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Corespunde GET /api/public/articles din admin. Header-ul x-api-key e adaugat
    // automat de interceptor-ul din NetworkModule, nu trebuie trecut aici.
    // Feed paginat: serverul trimite doar articolele paginii cerute (page incepe de la 1).
    @GET("api/public/articles")
    suspend fun getArticlesPage(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("section") section: String? = null,
    ): ArticlesPage

    // Incrementeaza view_count in admin - apelat cand userul deschide un articol.
    @POST("api/public/articles/{id}/view")
    suspend fun markViewed(@Path("id") articleId: String): ViewResponse
}

@Serializable
data class ViewResponse(val ok: Boolean = false)

@Serializable
data class ArticlesPage(
    val articles: List<Article>,
    val page: Int = 1,
    val pageSize: Int = 0,
    val total: Int = 0,
    val hasMore: Boolean = false,
)
