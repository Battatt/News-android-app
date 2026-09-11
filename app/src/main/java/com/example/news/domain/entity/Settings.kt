package com.example.news.domain.entity

data class Settings(
    val language: Language,
    val interval: Interval,
    val isNotificationsEnabled: Boolean,
    val isWifiOnly: Boolean,
) {

    companion object {

        val DEFAULT_LANGUAGE = Language.ENGLISH
        val DEFAULT_INTERVAL = Interval.MIN_15
        const val DEFAULT_IS_NOTIFICATION_ENABLED = false
        const val DEFAULT_IS_WIFI_ONLY = false
    }
}

enum class Language {
    ENGLISH, RUSSIAN, FRENCH, GERMAN,
}

enum class Interval(val minutes: Int) {
    MIN_15(15),
    MIN_30(30),
    HOUR_1(60),
    HOUR_2(120),
    HOUR_4(240),
    HOUR_8(480),
    HOUR_24(1440)
}
