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
    @GET("api/public/articles")
    suspend fun getArticles(@Query("section") section: String? = null): List<Article>

    // Incrementeaza view_count in admin - apelat cand userul deschide un articol.
    @POST("api/public/articles/{id}/view")
    suspend fun markViewed(@Path("id") articleId: String): ViewResponse
}

@Serializable
data class ViewResponse(val ok: Boolean = false)
