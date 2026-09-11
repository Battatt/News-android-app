package com.example.news.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.news.data.mapper.toInterval
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.Settings
import com.example.news.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val languageKey = stringPreferencesKey("language")
    private val intervalKey = intPreferencesKey("interval")
    private val isNotificationsEnabledKey = booleanPreferencesKey("is_notifications_enabled")
    private val isWifiOnlyKey = booleanPreferencesKey("is_wifi_only")


    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            val languageAsString = preferences[languageKey] ?: Settings.DEFAULT_LANGUAGE.name
            val language = Language.valueOf(languageAsString)
            val interval = preferences[intervalKey]?.toInterval() ?: Settings.DEFAULT_INTERVAL
            val isNotificationsEnabled =
                preferences[isNotificationsEnabledKey] ?: Settings.DEFAULT_IS_NOTIFICATION_ENABLED
            val isWifiOnly = preferences[isWifiOnlyKey] ?: Settings.DEFAULT_IS_WIFI_ONLY

            Settings(
                language = language,
                interval = interval,
                isNotificationsEnabled = isNotificationsEnabled,
                isWifiOnly = isWifiOnly
            )
        }
    }

    override suspend fun updateLanguage(language: Language) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[languageKey] = language.name
            }
        }
    }

    override suspend fun updateInterval(minutes: Int) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[intervalKey] = minutes
            }
        }
    }

    override suspend fun updateIsNotificationsEnabled(enabled: Boolean) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[isNotificationsEnabledKey] = enabled
            }
        }
    }

    override suspend fun updateIsWifiOnly(isWifiOnly: Boolean) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { mutablePreferences ->
                mutablePreferences[isWifiOnlyKey] = isWifiOnly
            }
        }
    }
}