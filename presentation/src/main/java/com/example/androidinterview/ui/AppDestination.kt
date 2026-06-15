package com.example.androidinterview.ui

import androidx.navigation3.runtime.NavKey
import com.example.androidinterview.domain.model.Merchant
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination : NavKey

@Serializable
data class RecentActivityDestination(val merchant: Merchant) : NavKey
