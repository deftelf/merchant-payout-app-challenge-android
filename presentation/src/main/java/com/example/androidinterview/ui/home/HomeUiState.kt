package com.example.androidinterview.ui.home

import com.example.androidinterview.domain.model.Merchant

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: Merchant) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
