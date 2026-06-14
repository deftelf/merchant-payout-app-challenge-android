package com.example.androidinterview.ui.activity

sealed class ActivityUiState {
    data class Success(val items: List<Item>) : ActivityUiState() {
        data class Item(
            val description: String,
            val value: String,
            val valueNegative: Boolean,
        )
    }
}
