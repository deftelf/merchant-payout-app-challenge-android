package com.example.androidinterview.domain.model

data class Merchant(
    val availableBalance: Int,
    val pendingBalance: Int,
    val currency: Currency,
    val activity: List<Activity>,
)

data class Activity(
    val id: String,
    val type: ActivityType,
    val amount: Int,
    val currency: Currency,
    val date: String,
    val description: String,
    val status: ActivityStatus,
)

enum class Currency { GBP, EUR }
enum class ActivityType { PAYOUT, DEPOSIT, REFUND, FEE }
enum class ActivityStatus { COMPLETED, PENDING, PROCESSING, FAILED }
