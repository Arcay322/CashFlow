package com.example.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.data.Transaction
import com.example.cashflow.ui.viewmodel.TransactionHistoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel = viewModel()
) {
    val transactions by viewModel.filteredTransactions.collectAsState(emptyList())
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            FilterBottomSheet(
                onApplyFilter = { type, category, startDate, endDate ->
                    viewModel.applyFilters(type, category, startDate, endDate)
                    coroutineScope.launch { modalBottomSheetState.hide() }
                },
                onClearFilters = {
                    viewModel.clearFilters()
                    coroutineScope.launch { modalBottomSheetState.hide() }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                onSearchQueryChange = { query -> viewModel.searchTransactions(query) },
                onFilterClick = {
                    coroutineScope.launch { modalBottomSheetState.show() }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransactionList(transactions = transactions)
        }
    }
}

@Composable
fun SearchBar(
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearchQueryChange(it)
            },
            label = { Text("Search Transactions") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onFilterClick) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter")
        }
    }
}

@Composable
fun TransactionList(transactions: List<Transaction>) {
    val groupedTransactions = transactions.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.date))
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        groupedTransactions.forEach { (date, transactionsForDate) ->
            item {
                Text(
                    text = formatDateHeader(date),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(transactionsForDate) { transaction ->
                TransactionItem(transaction = transaction)
                Divider()
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Replace with actual category icon
        Icon(Icons.Default.Search, contentDescription = transaction.category)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.description, style = MaterialTheme.typography.body1)
            Text(text = transaction.category, style = MaterialTheme.typography.caption)
        }
        Text(text = "$${transaction.amount}", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun FilterBottomSheet(
    onApplyFilter: (String?, String?, Long?, Long?) -> Unit,
    onClearFilters: () -> Unit
) {
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Filter Transactions", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Implement Type filter (e.g., Radio buttons)
        Text("Filter by Type")
        Row {
            Button(onClick = { selectedType = "Ingreso" }, enabled = selectedType != "Ingreso") { Text("Ingreso") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { selectedType = "Gasto" }, enabled = selectedType != "Gasto") { Text("Gasto") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { selectedType = null }, enabled = selectedType != null) { Text("All") }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Implement Category filter (e.g., dropdown or list)
        Text("Filter by Category")
        // Example: Replace with actual category selection
        TextField(value = selectedCategory ?: "", onValueChange = { selectedCategory = it }, label = { Text("Category") })
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Implement Date Range filter (e.g., date pickers)
        Text("Filter by Date Range")
        // Example: Replace with actual date pickers
        Button(onClick = { /* Show Start Date Picker */ }) { Text("Select Start Date") }
        Button(onClick = { /* Show End Date Picker */ }) { Text("Select End Date") }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onApplyFilter(selectedType, selectedCategory, startDate, endDate) }) {
                Text("Apply Filters")
            }
            Button(onClick = onClearFilters) {
                Text("Clear Filters")
            }
        }
    }
}


private fun formatDateHeader(dateString: String): String {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    val calendar = Calendar.getInstance().apply { time = date }

    return when {
        calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Hoy"
        calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> "Ayer"
        else -> SimpleDateFormat("dd 'de' MMMM", Locale.getDefault()).format(date)
    }
}