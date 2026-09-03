package com.example.news.data.repository

import android.util.Log
import com.example.news.data.local.ArticleDbModel
import com.example.news.data.local.NewsDao
import com.example.news.data.local.SubscriptionDbModel
import com.example.news.data.mapper.toDbModels
import com.example.news.data.mapper.toEntities
import com.example.news.data.remote.NewsApiService
import com.example.news.domain.entity.Article
import com.example.news.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService,
) : NewsRepository {

    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { subscriptions ->
            subscriptions.map {
                it.topic
            }
        }
    }

    override suspend fun addSubscription(topic: String) {
        newsDao.addSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForTopic(topic: String) {
        val articles = loadArticles(topic)
        newsDao.addArticles(articles)
    }

    private suspend fun loadArticles(topic: String): List<ArticleDbModel> {
        return runCatching {
            newsApiService.loadArticles(topic).toDbModels(topic)
        }.onFailure {
            if (it is CancellationException) {
                throw it
            }
            Log.e(
                ".data.repository.NewsRepository",
                it.stackTraceToString()
            )
        }.getOrElse { listOf() }
    }

    override suspend fun removeSubscription(topic: String) {
        newsDao.deleteSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForAllSubscriptions() {
        val subscriptions = newsDao.getAllSubscriptions().first()
        coroutineScope {
            subscriptions.forEach { subscription ->
                launch {
                    updateArticlesForTopic(subscription.topic)
                }
            }
        }
    }

    override fun getArticlesByTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopics(topics).map {
            it.toEntities()
        }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        newsDao.deleteArticlesByTopics(topics)
    }
}