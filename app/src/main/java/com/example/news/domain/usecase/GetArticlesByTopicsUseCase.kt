package com.example.news.domain.usecase

import com.example.news.domain.entity.Article
import com.example.news.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetArticlesByTopicsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(topics: List<String>): Flow<List<Article>> {
        return repository.getArticlesByTopics(topics)
    }
}