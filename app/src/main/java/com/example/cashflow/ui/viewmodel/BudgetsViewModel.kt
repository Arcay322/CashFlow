package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetWithProgress(
    val budget: Budget,
    val spentAmount: Double,
    val progress: Float
)

data class BudgetsUiState(
    val budgetsWithProgress: List<BudgetWithProgress> = emptyList(),
    val availableCategories: List<Category> = emptyList()
)

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val uiState: StateFlow<BudgetsUiState> = combine(
        budgetRepository.getAllBudgets(),
        transactionRepository.getExpensesByCategory(),
        categoryRepository.getCategoriesByType("Gasto")
    ) { budgets, expenses, categories ->
        val expenseMap = expenses.associate { it.category to it.total }
        val budgetCategoryNames = budgets.map { it.category }.toSet()
        val budgetsWithProgress = budgets.map { budget ->
            val spent = expenseMap[budget.category] ?: 0.0
            val progress = if (budget.amount > 0) (spent / budget.amount).toFloat() else 0f
            BudgetWithProgress(budget, spent, progress.coerceIn(0f, 1f))
        }
        BudgetsUiState(
            budgetsWithProgress = budgetsWithProgress,
            availableCategories = categories.filter { it.name !in budgetCategoryNames }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetsUiState()
    )

    fun createBudget(category: String, amount: Double) {
        viewModelScope.launch { budgetRepository.insertBudget(Budget(category = category, amount = amount)) }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch { budgetRepository.deleteBudget(budget) }
    }
}
