package com.example.androidinterview.data.model

import com.example.androidinterview.domain.model.Activity
import com.example.androidinterview.domain.model.ActivityStatus
import com.example.androidinterview.domain.model.ActivityType
import com.example.androidinterview.domain.model.Currency
import com.example.androidinterview.domain.model.Merchant

internal fun MerchantDto.toDomain() = Merchant(
    availableBalance = availableBalance,
    pendingBalance = pendingBalance,
    currency = currency.toDomain(),
    activity = activity.map { it.toDomain() },
)

internal fun ActivityItemDto.toDomain() = Activity(
    id = id,
    type = type.toDomain(),
    amount = amount,
    currency = currency.toDomain(),
    date = date,
    description = description,
    status = status.toDomain(),
)

internal fun CurrencyDto.toDomain() = when (this) {
    CurrencyDto.GBP -> Currency.GBP
    CurrencyDto.EUR -> Currency.EUR
}

internal fun ActivityTypeDto.toDomain() = when (this) {
    ActivityTypeDto.payout  -> ActivityType.PAYOUT
    ActivityTypeDto.deposit -> ActivityType.DEPOSIT
    ActivityTypeDto.refund  -> ActivityType.REFUND
    ActivityTypeDto.fee     -> ActivityType.FEE
}

internal fun ActivityStatusDto.toDomain() = when (this) {
    ActivityStatusDto.completed  -> ActivityStatus.COMPLETED
    ActivityStatusDto.pending    -> ActivityStatus.PENDING
    ActivityStatusDto.processing -> ActivityStatus.PROCESSING
    ActivityStatusDto.failed     -> ActivityStatus.FAILED
}
