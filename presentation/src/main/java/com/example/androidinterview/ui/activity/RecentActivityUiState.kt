package com.example.androidinterview.ui.activity

sealed class RecentActivityUiState {
    data class Success(val items: List<Item>) : RecentActivityUiState() {
        data class Item(
            val description: String,
            val value: String,
            val valueNegative: Boolean,
        )
    }
}
