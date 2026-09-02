package com.example.news.data.local

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "articles",
    primaryKeys = ["url", "topic"],
    foreignKeys = [
        ForeignKey(
            entity = SubscriptionDbModel::class,
            parentColumns = ["topic"],
            childColumns = ["topic"],
            onDelete = CASCADE,
        )
    ]
)
data class ArticleDbModel(
    val title: String,
    val description: String,
    val imageUrl: String?,
    val sourceName: String,
    val publishedAt: Long,
    val url: String,
    val topic: String
)
