package com.sportspulse.app.data.remote

import com.sportspulse.app.data.model.Article
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Corespunde GET /api/public/articles din admin. Header-ul x-api-key e adaugat
    // automat de interceptor-ul din NetworkModule, nu trebuie trecut aici.
    @GET("api/public/articles")
    suspend fun getArticles(@Query("section") section: String? = null): List<Article>
}
