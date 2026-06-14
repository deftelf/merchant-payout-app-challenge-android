package com.example.androidinterview.ui.home

import com.example.androidinterview.domain.model.Merchant

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: BusinessData) : HomeUiState() {
        data class BusinessData(
            val balanceAvailable: String,
            val balancePending: String,
            val recentActivity: List<Line>,
            val allActivity: List<Line>,
        ) {
            data class Line(
                val description: String,
                val value: String,
                val valueNegative: Boolean
            )
        }
    }
    data class Error(val message: String) : HomeUiState()
}
