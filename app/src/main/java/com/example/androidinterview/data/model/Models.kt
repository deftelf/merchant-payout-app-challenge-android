package com.example.androidinterview.data.model

import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Serializable
enum class Currency { GBP, EUR }

@Serializable
enum class ActivityType { payout, deposit, refund, fee }

@Serializable
enum class ActivityStatus { completed, pending, processing, failed }

@Serializable
data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val amount: Int,
    val currency: Currency,
    val date: String,
    val description: String,
    val status: ActivityStatus,
)

@Serializable
data class MerchantData(
    val available_balance: Int,
    val pending_balance: Int,
    val currency: Currency,
    val activity: List<ActivityItem>,
)

fun Currency.symbol(): String = when (this) {
    Currency.GBP -> "£"
    Currency.EUR -> "€"
}

fun formatAmount(currency: Currency, amountPence: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.UK).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val value = abs(amountPence) / 100.0
    val sign = if (amountPence < 0) "-" else "+"
    return "$sign${currency.symbol()}${formatter.format(value)}"
}
