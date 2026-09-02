package com.example.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("/v2/everything?apiKey=13788b20fe844468b194c4d7a6a7f4cf")
    suspend fun loadArticles(
        @Query("q") topic: String,
    ): NewsResponseDto
}