package com.example.news.data.mapper

import com.example.news.domain.entity.RefreshConfig
import com.example.news.domain.entity.Settings

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(
        language, interval, isWifiOnly
    )
}