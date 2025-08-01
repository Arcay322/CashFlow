package com.example.cashflow.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import java.util.Locale

// Renamed package to follow standard naming conventions
class DashboardViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // Data class to hold expense distribution data
    data class ExpenseDistribution(
        val category: String,
        val amount: Double
    )

    // Flow for the total balance for the current month
    // Combine total income and total expense to get the balance
    val currentMonthBalance: StateFlow<Double> =
        transactionRepository.getTotalIncome()
            .combine(transactionRepository.getTotalExpense()) { income, expense ->
                (income ?: 0.0) - (expense ?: 0.0)
            }
            .catch { emit(0.0) } // Handle errors by emitting a default value
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                // Set an initial value
                initialValue = 0.0
            )


    // Flow for recent transactions
    val recentTransactions = transactionRepository.getRecentTransactions(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            // Set an initial value
            initialValue = emptyList()
        )

    // Flow for expense distribution by category for the current month
    // Get the current month's start date in milliseconds
    private val currentMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val expenseDistribution: StateFlow<List<ExpenseDistribution>> =
        transactionRepository.getExpenseDistributionByCategory(currentMonth)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}