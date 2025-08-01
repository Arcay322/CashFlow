package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashflow.data.Transaction
import com.example.cashflow.data.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
class TransactionHistoryViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterType = MutableStateFlow<String?>(null)
    val filterType: StateFlow<String?> = _filterType.asStateFlow()

    private val _filterCategory = MutableStateFlow<String?>(null)
    val filterCategory: StateFlow<String?> = _filterCategory.asStateFlow()

    private val _filterDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val filterDateRange: StateFlow<Pair<Long, Long>?> = _filterDateRange.asStateFlow()

    val transactions: StateFlow<Map<Long, List<Transaction>>> = repository.getAllTransactions()
        .asStateFlow()
        .map { transactions -> transactions.groupBy { transaction -> getDateWithoutTime(transaction.date) }.toSortedMap(compareByDescending { it }) }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterType(type: String?) {
        _filterType.value = type
    }

    fun setFilterCategory(category: String?) {
        _filterCategory.value = category
    }

    fun setFilterDateRange(startDate: Long?, endDate: Long?) {
        _filterDateRange.value = if (startDate != null && endDate != null) Pair(startDate, endDate) else null
    }

    private fun getDateWithoutTime(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0).set(Calendar.SECOND, 0).set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}