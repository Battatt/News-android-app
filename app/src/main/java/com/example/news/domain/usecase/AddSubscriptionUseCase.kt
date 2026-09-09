package com.example.news.domain.usecase

import com.example.news.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class AddSubscriptionUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(topic: String) {
        repository.addSubscription(topic)
        CoroutineScope(currentCoroutineContext()).launch {
            repository.updateArticlesForTopic(topic)
        }
    }
}