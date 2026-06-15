package com.example.androidinterview.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.repository.MerchantRepository
import com.example.androidinterview.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
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
                    else -> HomeUiState.Success(data = uiModelMapper(merchant))
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
                .onFailure { networkError.value = it.message ?: context.getString(R.string.error_unknown) }
        }
    }
}
