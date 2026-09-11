package com.example.news.domain.usecase

import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIsWifiOnlyUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(isWifiOnly: Boolean) {
        settingsRepository.updateIsWifiOnly(isWifiOnly)
    }
}