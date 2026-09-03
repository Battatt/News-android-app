@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.news.presentation.screens.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.entity.Article
import com.example.news.domain.usecase.AddSubscriptionUseCase
import com.example.news.domain.usecase.ClearAllArticlesUseCase
import com.example.news.domain.usecase.GetAllSubscriptionsUseCase
import com.example.news.domain.usecase.GetArticlesByTopicsUseCase
import com.example.news.domain.usecase.RemoveSubscriptionUseCase
import com.example.news.domain.usecase.UpdateSubscribedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val clearAllArticlesUseCase: ClearAllArticlesUseCase,
    private val getAllSubscriptionsUseCase: GetAllSubscriptionsUseCase,
    private val getArticlesByTopicsUseCase: GetArticlesByTopicsUseCase,
    private val removeSubscriptionUseCase: RemoveSubscriptionUseCase,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SubscriptionState())
    val state = _state.asStateFlow()

    init {
        observeSubscriptions()
        observeSelectedTopics()
    }


    fun processCommand(command: SubscriptionsCommand)  {
        when (command) {
            SubscriptionsCommand.ClearArticles -> {
                val topics = state.value.selectedTopics
                viewModelScope.launch {
                    clearAllArticlesUseCase(topics)
                }
            }
            SubscriptionsCommand.ClickSubscribe -> {
                viewModelScope.launch {
                    val query = state.value.query.trim()
                    addSubscriptionUseCase(query)
                    _state.update { previousState ->
                        previousState.copy(query = "")
                    }
                }
            }
            is SubscriptionsCommand.InputTopic -> {
                _state.update { previousState ->
                    previousState.copy(query = command.query)
                }
            }
            SubscriptionsCommand.RefreshData -> {
                viewModelScope.launch {
                    updateSubscribedArticlesUseCase()
                }
            }
            is SubscriptionsCommand.RemoveSubscription -> {
                viewModelScope.launch {
                    removeSubscriptionUseCase(command.topic)
                }
            }
            is SubscriptionsCommand.ToggleTopicSelection -> {
                _state.update { previousState ->
                    val subscriptions = previousState.subscriptions.toMutableMap()
                    val isSelected = subscriptions[command.topic] ?: false
                    subscriptions[command.topic] = !isSelected
                    previousState.copy(subscriptions = subscriptions)
                }
            }
        }
    }

    private fun observeSelectedTopics() {
        state.map {
            it.selectedTopics
        }.distinctUntilChanged()
            .flatMapLatest {
                getArticlesByTopicsUseCase(it)
            }
            .onEach {
                _state.update { previousState ->
                    previousState.copy(articles = it)
                }
            }.launchIn(viewModelScope)
    }

    private fun observeSubscriptions() {
        getAllSubscriptionsUseCase().onEach { subscription ->
            _state.update { previousState ->
                val updatedTopics = subscription.associateWith { topic ->
                    previousState.subscriptions[topic] ?: true
                }
                previousState.copy(subscriptions =  updatedTopics)
            }
        }.launchIn(viewModelScope)
    }
}

data class SubscriptionState(
    val query: String = "",
    val subscriptions: Map<String, Boolean> = mapOf(),
    val articles: List<Article> = listOf()
) {

    val selectedTopics: List<String>
        get() {
            return subscriptions.filter { it.value }.map { it.key }
        }
    val isSubscribeButtonEnabled: Boolean
        get() = query.isNotBlank()
}

sealed interface SubscriptionsCommand {
    data class InputTopic(
        val query: String,
    ) : SubscriptionsCommand

    data object ClickSubscribe : SubscriptionsCommand
    data object RefreshData : SubscriptionsCommand
    data class ToggleTopicSelection(val topic: String) : SubscriptionsCommand
    data object ClearArticles : SubscriptionsCommand
    data class RemoveSubscription(val topic: String) : SubscriptionsCommand
}