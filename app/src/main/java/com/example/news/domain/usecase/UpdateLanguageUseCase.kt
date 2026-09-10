package com.example.news.domain.usecase

import com.example.news.domain.entity.Interval
import com.example.news.domain.entity.Language
import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: Language) {
        settingsRepository.updateLanguage(language)
    }
}