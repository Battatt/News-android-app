package com.example.news.domain.repository

import com.example.news.domain.entity.Language
import com.example.news.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)
    suspend fun updateInterval(minutes: Int)
    suspend fun updateIsNotificationsEnabled(enabled: Boolean)
    suspend fun updateIsWifiOnly(isWifiOnly: Boolean)
}