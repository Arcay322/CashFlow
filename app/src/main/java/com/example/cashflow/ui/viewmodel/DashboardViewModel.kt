package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.CategoryExpense
import com.example.cashflow.data.Transaction
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

    val uiState: StateFlow<DashboardUiState> = combine(
        transactionRepository.getRecentTransactions(5),
        transactionRepository.getTotalIncome(),
        transactionRepository.getTotalExpense(),
        transactionRepository.getExpensesByCategory()
    ) { recent, income, expense, distribution ->
        DashboardUiState(
            balance = income - expense,
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
    val recentTransactions: List<Transaction> = emptyList(),
    val expenseDistribution: List<CategoryExpense> = emptyList()
)
