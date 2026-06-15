package com.example.androidinterview.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidinterview.domain.model.Merchant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    merchant: Merchant,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ActivityViewModel = hiltViewModel<ActivityViewModel, ActivityViewModelFactory>(
        creationCallback = { factory -> factory.create(merchant) },
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val items = (uiState as? ActivityUiState.Success)?.items.orEmpty()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("All Activity") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {
            items(items, key = { it.description + it.value }) { item ->
                ActivityRow(item = item)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun ActivityRow(item: ActivityUiState.Success.Item) {
    val amountColor = if (item.valueNegative) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
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
            text = item.value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = amountColor,
        )
    }
}
