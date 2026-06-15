package com.example.androidinterview.ui.activity

import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.util.formatAmount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityUiModelMapper @Inject constructor() {

    operator fun invoke(merchant: Merchant): ActivityUiState.Success {
        return ActivityUiState.Success(
            items = merchant.activity.map {
                ActivityUiState.Success.Item(
                    description = it.description,
                    value = formatAmount(it.currency, it.amount),
                    valueNegative = it.amount < 0,
                )
            },
        )
    }
}
