package com.example.androidinterview.ui.activity

import com.example.androidinterview.domain.model.Activity
import com.example.androidinterview.domain.util.formatAmount
import com.example.androidinterview.ui.activity.RecentActivityUiState.Success.ActivityGroup
import com.example.androidinterview.ui.activity.RecentActivityUiState.Success.Item
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentActivityUiModelMapper @Inject constructor() {

    private val itemDateFormatter = DateTimeFormatter.ofPattern("dd MM yyyy")
    private val groupDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    operator fun invoke(activities: List<Activity>): List<ActivityGroup> {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        return activities
            .groupBy { ZonedDateTime.parse(it.date).toLocalDate() }
            .entries
            .sortedByDescending { it.key }
            .map { (date, items) ->
                ActivityGroup(
                    label = when (date) {
                        today -> "Today"
                        yesterday -> "Yesterday"
                        else -> date.format(groupDateFormatter)
                    },
                    items = items.map { activity ->
                        Item(
                            description = activity.description,
                            type = activity.type.name.lowercase() // TOOD map properly
                                .replaceFirstChar { it.uppercase() },
                            value = formatAmount(activity.currency, activity.amount),
                            valueNegative = activity.amount < 0,
                            date = ZonedDateTime.parse(activity.date)
                                .toLocalDate()
                                .format(itemDateFormatter),
                            status = activity.status.name.lowercase() // TOOD map properly
                                .replaceFirstChar { it.uppercase() },
                        )
                    },
                )
            }
    }
}
