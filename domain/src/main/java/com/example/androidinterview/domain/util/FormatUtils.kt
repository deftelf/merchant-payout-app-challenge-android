package com.example.androidinterview.domain.util

import com.example.androidinterview.domain.model.Currency
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

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
