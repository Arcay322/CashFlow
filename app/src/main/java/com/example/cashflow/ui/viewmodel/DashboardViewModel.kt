package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.Transaction
import com.example.cashflow.data.TransactionDao
import com.example.cashflow.data.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val recentTransactions = transactionRepository.getRecentTransactions(5)
    private val totalIncome = transactionRepository.getTotalIncome()
    private val totalExpense = transactionRepository.getTotalExpense()
    private val expenseDistribution = transactionRepository.getExpenseByCategory()

    val uiState: StateFlow<DashboardUiState> = combine(
        recentTransactions,
        totalIncome,
        totalExpense,
        expenseDistribution
    ) { recent, income, expense, distribution ->
        DashboardUiState(
            balance = (income ?: 0.0) - (expense ?: 0.0),
            totalIncome = income ?: 0.0,
            totalExpense = expense ?: 0.0,
            recentTransactions = recent,
            expenseDistribution = distribution
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}

data class DashboardUiState(
    val balance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val expenseDistribution: List<TransactionDao.CategoryExpense> = emptyList()
)
