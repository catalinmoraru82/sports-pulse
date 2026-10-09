package com.sportspulse.app.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportspulse.app.data.model.Article
import com.sportspulse.app.data.repository.ArticleRepository
import com.sportspulse.app.data.repository.ArticlesPageResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Cate articole se cer odata - la prima incarcare si la fiecare "lot" urmator din scroll.
private const val PAGE_SIZE = 10

sealed interface FeedUiState {
    data object Loading : FeedUiState
    data class Success(
        val articles: List<Article>,
        val hasMore: Boolean,
        val isLoadingMore: Boolean = false,
        val loadMoreFailed: Boolean = false,
    ) : FeedUiState
    data class Error(val message: String) : FeedUiState
}

class FeedViewModel(
    private val repository: ArticleRepository = ArticleRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var nextPage = 1
    private var currentSection: String? = null
    private var loadMoreJob: Job? = null

    init {
        loadArticles()
    }

    fun loadArticles(section: String? = null) {
        loadMoreJob?.cancel()
        currentSection = section
        viewModelScope.launch {
            _uiState.value = FeedUiState.Loading
            fetchFirstPage()
        }
    }

    fun refresh(section: String? = currentSection) {
        loadMoreJob?.cancel()
        currentSection = section
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchFirstPage()
            _isRefreshing.value = false
        }
    }

    // Cere urmatorul lot de articole. Apelat automat cand userul ajunge aproape de finalul
    // listei (sau de butonul "Incearca din nou" daca lotul anterior a esuat).
    fun loadMore() {
        val state = _uiState.value as? FeedUiState.Success ?: return
        if (!state.hasMore || state.isLoadingMore || _isRefreshing.value) return

        // starea se seteaza sincron, inainte de coroutine - scroll-ul poate apela de mai multe ori
        _uiState.value = state.copy(isLoadingMore = true, loadMoreFailed = false)

        loadMoreJob = viewModelScope.launch {
            when (val result = repository.getArticlesPage(nextPage, PAGE_SIZE, currentSection)) {
                is ArticlesPageResult.Success -> {
                    nextPage++
                    _uiState.update { current ->
                        if (current !is FeedUiState.Success) return@update current
                        // distinctBy: daca s-au publicat articole noi intre timp, paginile se
                        // deplaseaza si un articol poate aparea de doua ori - cheile duplicate
                        // ar crapa LazyColumn-ul.
                        current.copy(
                            articles = (current.articles + result.articles).distinctBy { it.id },
                            hasMore = result.hasMore,
                            isLoadingMore = false,
                        )
                    }
                }
                is ArticlesPageResult.Error -> _uiState.update { current ->
                    if (current is FeedUiState.Success) {
                        current.copy(isLoadingMore = false, loadMoreFailed = true)
                    } else {
                        current
                    }
                }
            }
        }
    }

    private suspend fun fetchFirstPage() {
        when (val result = repository.getArticlesPage(1, PAGE_SIZE, currentSection)) {
            is ArticlesPageResult.Success -> {
                nextPage = 2
                _uiState.value = FeedUiState.Success(
                    articles = result.articles.distinctBy { it.id },
                    hasMore = result.hasMore,
                )
            }
            is ArticlesPageResult.Error -> _uiState.value = FeedUiState.Error(result.message)
        }
    }
}
