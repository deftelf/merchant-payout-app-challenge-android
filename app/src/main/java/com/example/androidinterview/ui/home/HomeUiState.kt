package com.example.androidinterview.ui.home

import com.example.androidinterview.data.model.MerchantData

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: MerchantData) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
