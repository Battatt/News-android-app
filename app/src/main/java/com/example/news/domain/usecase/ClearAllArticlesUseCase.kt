package com.example.news.domain.usecase

import com.example.news.domain.repository.NewsRepository
import jakarta.inject.Inject

class ClearAllArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(topics: List<String>) {
        repository.clearAllArticles(topics)
    }
}