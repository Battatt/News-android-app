package com.example.news.domain.usecase

import com.example.news.domain.repository.NewsRepository
import jakarta.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke() = repository.updateArticlesForAllSubscriptions()
}