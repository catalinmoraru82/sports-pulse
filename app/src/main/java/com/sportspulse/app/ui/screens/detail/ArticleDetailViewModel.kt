package com.sportspulse.app.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportspulse.app.data.model.Article
import com.sportspulse.app.data.repository.ArticleRepository
import com.sportspulse.app.data.repository.ArticlesResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ArticleDetailUiState {
    data object Loading : ArticleDetailUiState
    data class Success(val article: Article) : ArticleDetailUiState
    data class Error(val message: String) : ArticleDetailUiState
}

class ArticleDetailViewModel(
    private val repository: ArticleRepository = ArticleRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleDetailUiState>(ArticleDetailUiState.Loading)
    val uiState: StateFlow<ArticleDetailUiState> = _uiState.asStateFlow()

    private var loadedId: String? = null

    fun load(articleId: String) {
        if (loadedId == articleId) return // evita reload la recompunere
        loadedId = articleId

        val cached = ArticleRepository.getCached(articleId)
        if (cached != null) {
            _uiState.value = ArticleDetailUiState.Success(cached)
            return
        }

        // Fallback: articolul nu era in cache (ex: deschis dintr-un deep link, sau
        // process death) - reincarcam tot feed-ul si cautam id-ul in rezultat.
        viewModelScope.launch {
            _uiState.value = ArticleDetailUiState.Loading
            when (val result = repository.getArticles()) {
                is ArticlesResult.Success -> {
                    val found = result.articles.find { it.id == articleId }
                    _uiState.value = if (found != null) {
                        ArticleDetailUiState.Success(found)
                    } else {
                        ArticleDetailUiState.Error("Articolul nu a fost gasit.")
                    }
                }
                is ArticlesResult.Error -> {
                    _uiState.value = ArticleDetailUiState.Error(result.message)
                }
            }
        }
    }
}
