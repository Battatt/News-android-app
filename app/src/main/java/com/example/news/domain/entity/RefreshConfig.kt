package com.example.news.domain.entity

data class RefreshConfig(
    val language: Language,
    val interval: Interval,
    val isWifiOnly: Boolean,
)
