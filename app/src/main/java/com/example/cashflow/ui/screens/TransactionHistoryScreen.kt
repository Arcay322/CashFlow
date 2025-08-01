package com.example.cashflow.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashflow.data.Transaction
import com.example.cashflow.ui.viewmodel.HistoryUiState
import com.example.cashflow.ui.viewmodel.TransactionHistoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBarAndFilters(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onFilterTypeChange = viewModel::onFilterTypeChange
        )

        if (uiState.transactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay transacciones que coincidan con tu búsqueda.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                uiState.transactions.forEach { (date, transactionsOnDate) ->
                    stickyHeader {
                        DateHeader(date = date)
                    }
                    items(transactionsOnDate) { transaction ->
                        HistoryTransactionItem(transaction = transaction)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBarAndFilters(
    uiState: HistoryUiState,
    onSearchQueryChange: (String) -> Unit,
    onFilterTypeChange: (String?) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar por descripción o categoría...") },
            singleLine = true,
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        FilterChipGroup(
            selectedType = uiState.filterType,
            onFilterChange = onFilterTypeChange
        )
    }
}

@Composable
private fun FilterChipGroup(selectedType: String?, onFilterChange: (String?) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        FilterChip(
            selected = selectedType == null,
            onClick = { onFilterChange(null) },
            label = { Text("Todos") }
        )
        FilterChip(
            selected = selectedType == "Ingreso",
            onClick = { onFilterChange("Ingreso") },
            label = { Text("Ingresos") }
        )
        FilterChip(
            selected = selectedType == "Gasto",
            onClick = { onFilterChange("Gasto") },
            label = { Text("Gastos") }
        )
    }
}

@Composable
private fun DateHeader(date: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun HistoryTransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Aquí podrías poner un icono basado en la categoría
        // Icon(...)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = formatCurrency(transaction.amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (transaction.type == "Ingreso") Color(0xFF2E7D32) else Color(0xFFC62828)
        )
    }
}
