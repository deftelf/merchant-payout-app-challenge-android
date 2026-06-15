package com.example.androidinterview.ui.payout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidinterview.domain.model.Currency
import com.example.androidinterview.domain.repository.PayoutRepository
import com.example.androidinterview.domain.util.formatAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class PayoutViewModel @Inject constructor(
    private val repository: PayoutRepository,
) : ViewModel() {

    val amountInput = MutableStateFlow("")
    val currency = MutableStateFlow(Currency.GBP)
    val ibanInput = MutableStateFlow("")

    private val _uiState = MutableStateFlow<PayoutUiState>(PayoutUiState.Idle)
    val uiState: StateFlow<PayoutUiState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = combine(amountInput, ibanInput) { amount, iban ->
        amount.toDoubleOrNull()?.let { it > 0 } == true && isValidIban(iban)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onRequestPayout() {
        val pence = ((amountInput.value.toDoubleOrNull() ?: return) * 100).roundToInt()
        _uiState.value = PayoutUiState.Confirming(
            formattedAmount = formatAmount(currency.value, pence),
            currency = currency.value,
            iban = ibanInput.value,
            amountPence = pence,
        )
    }

    fun onConfirm() {
        val state = _uiState.value as? PayoutUiState.Confirming ?: return
        _uiState.value = PayoutUiState.Submitting
        viewModelScope.launch {
            repository.createPayout(state.amountPence, state.currency, state.iban)
                .onSuccess { payout ->
                    _uiState.value = PayoutUiState.Success(
                        formattedAmount = formatAmount(payout.currency, payout.amount),
                        currency = payout.currency,
                        iban = payout.iban,
                    )
                }
                .onFailure {
                    _uiState.value = PayoutUiState.Error(it.message ?: "Something went wrong")
                }
        }
    }

    fun onCancel() { _uiState.value = PayoutUiState.Idle }

    fun onReset() {
        amountInput.value = ""
        ibanInput.value = ""
        currency.value = Currency.GBP
        _uiState.value = PayoutUiState.Idle
    }

    fun onRetry() { _uiState.value = PayoutUiState.Idle }
}

private fun isValidIban(iban: String): Boolean =
    iban.matches(Regex("^[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}$"))
