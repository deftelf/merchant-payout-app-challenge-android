package com.example.androidinterview.ui.payout

import com.example.androidinterview.domain.model.Currency

sealed class PayoutUiState {
    object Idle : PayoutUiState()
    data class Confirming(
        val formattedAmount: String,
        val currency: Currency,
        val iban: String,
        val amountPence: Int,
    ) : PayoutUiState()
    data class AwaitingBiometric(
        val formattedAmount: String,
        val currency: Currency,
        val iban: String,
        val amountPence: Int,
    ) : PayoutUiState()
    object Submitting : PayoutUiState()
    data class Success(
        val formattedAmount: String,
        val currency: Currency,
        val iban: String,
    ) : PayoutUiState()
    data class Error(val message: String) : PayoutUiState()
}
