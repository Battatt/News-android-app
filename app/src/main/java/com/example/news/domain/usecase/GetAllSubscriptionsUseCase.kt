package com.example.news.domain.usecase

import com.example.news.domain.repository.NewsRepository
import jakarta.inject.Inject

class GetAllSubscriptionsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke() = repository.getAllSubscriptions()
}