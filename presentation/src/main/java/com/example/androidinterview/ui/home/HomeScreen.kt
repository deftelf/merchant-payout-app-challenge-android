package com.example.androidinterview.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidinterview.domain.model.Activity
import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.util.formatAmount
import com.example.androidinterview.domain.util.symbol

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is HomeUiState.Loading -> LoadingContent(modifier = modifier)
        is HomeUiState.Error -> ErrorContent(modifier = modifier, message = state.message, onRetry = viewModel::loadData)
        is HomeUiState.Success -> MerchantContent(modifier = modifier, data = state.data)
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(modifier: Modifier = Modifier, message: String, onRetry: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MerchantContent(modifier: Modifier = Modifier, data: Merchant) {
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        BalanceCard(data = data)
        Spacer(modifier = Modifier.height(24.dp))
        RecentActivitySection(
            activity = data.activity,
            onShowMore = { showModal = true },
        )
    }

    if (showModal) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showModal = false },
            sheetState = sheetState,
        ) {
            ActivityModal(activity = data.activity)
        }
    }
}

@Composable
private fun BalanceCard(data: Merchant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Account Balance",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            BalanceRow(
                label = "Available Balance",
                amount = formatAmount(data.currency, data.availableBalance).removePrefix("+"),
            )
            Spacer(modifier = Modifier.height(8.dp))
            BalanceRow(
                label = "Pending Balance",
                amount = formatAmount(data.currency, data.pendingBalance).removePrefix("+"),
            )
        }
    }
}

@Composable
private fun BalanceRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = amount, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RecentActivitySection(activity: List<Activity>, onShowMore: () -> Unit) {
    Column {
        Text(
            text = "Recent Activity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        activity.forEach { item ->
            ActivityRow(item = item)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            onClick = onShowMore,
            modifier = Modifier.align(Alignment.End),
        ) {
            Text("Show more")
        }
    }
}

@Composable
private fun ActivityModal(activity: List<Activity>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "All Activity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        LazyColumn {
            items(activity, key = { it.id }) { item ->
                ActivityRow(item = item)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun ActivityRow(item: Activity) {
    val amountColor = if (item.amount >= 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = formatAmount(item.currency, item.amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = amountColor,
        )
    }
}
