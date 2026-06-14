package com.example.androidinterview.ui.home

import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.util.formatAmount
import com.example.androidinterview.domain.util.symbol
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUiModelMapper @Inject constructor() {

    operator fun invoke(
        merchant: Merchant,
    ) : HomeUiState.Success.BusinessData {
        return HomeUiState.Success.BusinessData(
            balanceAvailable = formatAmount(merchant.currency, merchant.availableBalance),
            balancePending = formatAmount(merchant.currency, merchant.pendingBalance),
            recentActivity = merchant.activity.take(3).map {
                HomeUiState.Success.BusinessData.Line(it.description, formatAmount(it.currency, it.amount), it.amount < 0)
            }
        )
    }

}