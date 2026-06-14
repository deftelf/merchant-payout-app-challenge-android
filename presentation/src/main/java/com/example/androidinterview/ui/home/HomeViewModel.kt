package com.example.androidinterview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.repository.MerchantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MerchantRepository,
    private val uiModelMapper: HomeUiModelMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val merchant = MutableStateFlow<Merchant?>(null)
    private val networkError = MutableStateFlow<String?>(null)

    init {
        loadData()
        generateUi()
    }

    fun generateUi() {
        viewModelScope.launch {
            combine(merchant, networkError) { merchant, networkError ->
                when {
                    networkError != null -> HomeUiState.Error(networkError)
                    merchant == null -> HomeUiState.Loading
                    else -> HomeUiState.Success(data = uiModelMapper(merchant), merchant = merchant)
                }
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            networkError.value = null
            repo.getMerchant()
                .onSuccess { merchant.value = it }
                .onFailure { networkError.value = it.message ?: "Unknown error" }
        }
    }
}
