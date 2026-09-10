package com.example.news.domain.usecase

import com.example.news.domain.entity.Interval
import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(interval: Interval) {
        settingsRepository.updateInterval(interval.minutes)
    }
}