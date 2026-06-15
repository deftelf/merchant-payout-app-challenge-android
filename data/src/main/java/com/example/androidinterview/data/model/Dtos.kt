package com.example.androidinterview.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal enum class CurrencyDto {
    @SerialName("GBP") GBP,
    @SerialName("EUR") EUR,
}

@Serializable
internal enum class ActivityTypeDto {
    @SerialName("payout") PAYOUT,
    @SerialName("deposit") DEPOSIT,
    @SerialName("refund") REFUND,
    @SerialName("fee") FEE,
}

@Serializable
internal enum class ActivityStatusDto {
    @SerialName("completed") COMPLETED,
    @SerialName("pending") PENDING,
    @SerialName("processing") PROCESSING,
    @SerialName("failed") FAILED,
}

@Serializable
internal data class ActivityItemDto(
    @SerialName("id") val id: String,
    @SerialName("type") val type: ActivityTypeDto,
    @SerialName("amount") val amount: Int,
    @SerialName("currency") val currency: CurrencyDto,
    @SerialName("date") val date: String,
    @SerialName("description") val description: String,
    @SerialName("status") val status: ActivityStatusDto,
)

@Serializable
internal data class MerchantDto(
    @SerialName("available_balance") val availableBalance: Int,
    @SerialName("pending_balance") val pendingBalance: Int,
    @SerialName("currency") val currency: CurrencyDto,
    @SerialName("activity") val activity: List<ActivityItemDto>,
)
