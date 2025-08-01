package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.Transaction
import com.example.cashflow.data.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class HistoryUiState(
    val transactions: Map<String, List<Transaction>> = emptyMap(),
    val searchQuery: String = "",
    val filterType: String? = null // "Ingreso", "Gasto", or null for both
)

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _filterType = MutableStateFlow<String?>(null)

    // El estado de la UI combina las transacciones originales con los filtros
    val uiState: StateFlow<HistoryUiState> = combine(
        transactionRepository.getAllTransactions(),
        _searchQuery,
        _filterType
    ) { allTransactions, query, type ->
        val filteredTransactions = allTransactions.filter { transaction ->
            val matchesSearch = if (query.isBlank()) {
                true
            } else {
                transaction.description.contains(query, ignoreCase = true) ||
                transaction.category.contains(query, ignoreCase = true)
            }
            val matchesType = type == null || transaction.type == type
            
            matchesSearch && matchesType
        }
        
        HistoryUiState(
            transactions = filteredTransactions.groupBy { getFormattedDate(it.date) },
            searchQuery = query,
            filterType = type
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onFilterTypeChange(newType: String?) {
        _filterType.value = newType
    }
    
    // Función para agrupar las fechas por "Hoy", "Ayer" y "dd MMMM"
    private fun getFormattedDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale("es", "ES"))
        val transactionDate = Calendar.getInstance().apply { timeInMillis = timestamp }
        val today = Calendar.getInstance()
        
        return when {
            isSameDay(transactionDate, today) -> "Hoy"
            isYesterday(transactionDate, today) -> "Ayer"
            else -> sdf.format(transactionDate.time)
        }
    }
    
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(cal1: Calendar, cal2: Calendar): Boolean {
        cal1.add(Calendar.DAY_OF_YEAR, 1)
        return isSameDay(cal1, cal2)
    }
}
