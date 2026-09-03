package com.example.news.data.mapper

import com.example.news.data.local.ArticleDbModel
import com.example.news.data.remote.NewsResponseDto
import com.example.news.domain.entity.Article
import java.text.SimpleDateFormat
import java.util.Locale


fun NewsResponseDto.toDbModels(topic: String): List<ArticleDbModel> {
    return articles.map {
        ArticleDbModel(
            title = it.title,
            url = it.url,
            imageUrl = it.urlToImage,
            sourceName = it.source.name,
            topic = topic,
            publishedAt = it.publishedAt.toTimeStamp(),
            description = it.description,
        )
    }
}

fun List<ArticleDbModel>.toEntities(): List<Article> {
    return map { model ->
        Article(
            title = model.title,
            description = model.description,
            publishedAt = model.publishedAt,
            sourceName = model.sourceName,
            imageUrl = model.imageUrl,
            url = model.url
        )
    }.distinct()
}


private fun String.toTimeStamp(): Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}