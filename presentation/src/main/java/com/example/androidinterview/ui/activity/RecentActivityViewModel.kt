package com.example.androidinterview.ui.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidinterview.domain.model.Activity
import com.example.androidinterview.domain.repository.MerchantRepository
import com.example.androidinterview.presentation.R
import com.example.androidinterview.ui.activity.RecentActivityUiState.Error
import com.example.androidinterview.ui.activity.RecentActivityUiState.Loading
import com.example.androidinterview.ui.activity.RecentActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentActivityViewModel @Inject constructor(
    private val repository: MerchantRepository,
    private val mapper: RecentActivityUiModelMapper,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecentActivityUiState>(Loading)
    val uiState: StateFlow<RecentActivityUiState> = _uiState.asStateFlow()

    private val loadedItems = mutableListOf<Activity>()
    private var nextCursor: String? = null
    private var hasMore = true
    private var isLoadingMore = false

    init {
        loadPage(cursor = null, isInitial = true)
    }

    fun loadMore() {
        if (hasMore && !isLoadingMore) loadPage(cursor = nextCursor, isInitial = false)
    }

    fun retry() {
        loadedItems.clear()
        nextCursor = null
        hasMore = true
        loadPage(cursor = null, isInitial = true)
    }

    private fun loadPage(cursor: String?, isInitial: Boolean) {
        isLoadingMore = true
        if (isInitial) {
            _uiState.value = Loading
        } else {
            (_uiState.value as? Success)?.let { _uiState.value = it.copy(isLoadingMore = true) }
        }
        viewModelScope.launch {
            repository.getActivity(cursor = cursor)
                .onSuccess { page ->
                    loadedItems.addAll(page.items)
                    nextCursor = page.nextCursor
                    hasMore = page.hasMore
                    isLoadingMore = false
                    _uiState.value = Success(
                        groups = mapper(loadedItems),
                        isLoadingMore = false,
                        hasMore = hasMore,
                    )
                }
                .onFailure {
                    isLoadingMore = false
                    if (loadedItems.isEmpty()) {
                        _uiState.value = Error(it.message ?: context.getString(R.string.error_something_went_wrong))
                    } else {
                        (_uiState.value as? Success)?.let { s ->
                            _uiState.value = s.copy(isLoadingMore = false)
                        }
                    }
                }
        }
    }
}
