package com.example.androidinterview.ui.activity

import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.util.formatAmount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentActivityUiModelMapper @Inject constructor() {

    operator fun invoke(merchant: Merchant): RecentActivityUiState.Success {
        return RecentActivityUiState.Success(
            items = merchant.activity.map {
                RecentActivityUiState.Success.Item(
                    description = it.description,
                    value = formatAmount(it.currency, it.amount),
                    valueNegative = it.amount < 0,
                )
            },
        )
    }
}
