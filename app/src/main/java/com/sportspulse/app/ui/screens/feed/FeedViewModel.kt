package com.sportspulse.app.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportspulse.app.data.model.Article
import com.sportspulse.app.data.repository.ArticleRepository
import com.sportspulse.app.data.repository.ArticlesResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface FeedUiState {
    data object Loading : FeedUiState
    data class Success(val articles: List<Article>) : FeedUiState
    data class Error(val message: String) : FeedUiState
}

class FeedViewModel(
    private val repository: ArticleRepository = ArticleRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles(section: String? = null) {
        viewModelScope.launch {
            _uiState.value = FeedUiState.Loading
            fetch(section)
        }
    }

    fun refresh(section: String? = null) {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetch(section)
            _isRefreshing.value = false
        }
    }

    private suspend fun fetch(section: String?) {
        when (val result = repository.getArticles(section)) {
            is ArticlesResult.Success -> _uiState.update { FeedUiState.Success(result.articles) }
            is ArticlesResult.Error -> _uiState.update { FeedUiState.Error(result.message) }
        }
    }
}
