package com.example.androidinterview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal enum class CurrencyDto { GBP, EUR }

@Serializable
internal enum class ActivityTypeDto { payout, deposit, refund, fee }

@Serializable
internal enum class ActivityStatusDto { completed, pending, processing, failed }

@Serializable
internal data class ActivityItemDto(
    val id: String,
    val type: ActivityTypeDto,
    val amount: Int,
    val currency: CurrencyDto,
    val date: String,
    val description: String,
    val status: ActivityStatusDto,
)

@Serializable
internal data class MerchantDto(
    @SerialName("available_balance") val availableBalance: Int,
    @SerialName("pending_balance") val pendingBalance: Int,
    val currency: CurrencyDto,
    val activity: List<ActivityItemDto>,
)
