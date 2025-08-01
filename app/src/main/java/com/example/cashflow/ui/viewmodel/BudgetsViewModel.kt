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
    val progress: Float // 0.0f to 1.0f
)

data class BudgetsUiState(
    val budgetsWithProgress: List<BudgetWithProgress> = emptyList(),
    val availableCategories: List<Category> = emptyList()
)

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository // Necesario para obtener las categorías de gastos
) : ViewModel() {

    private val allBudgets = budgetRepository.getAllBudgets()
    private val expenseByCategory = transactionRepository.getExpenseByCategory()
    
    // Filtramos las categorías para mostrar solo las de "Gasto" que aún no tienen un presupuesto
    private val availableCategories = combine(
        categoryRepository.getCategoriesByType("Gasto"),
        allBudgets
    ) { categories, budgets ->
        val budgetCategoryNames = budgets.map { it.category }.toSet()
        categories.filter { it.name !in budgetCategoryNames }
    }

    val uiState: StateFlow<BudgetsUiState> = combine(
        allBudgets,
        expenseByCategory,
        availableCategories
    ) { budgets, expenses, categories ->
        val expenseMap = expenses.associate { it.category to it.total }

        val budgetsWithProgress = budgets.map { budget ->
            val spent = expenseMap[budget.category] ?: 0.0
            val progress = if (budget.amount > 0) (spent / budget.amount).toFloat() else 0f
            BudgetWithProgress(
                budget = budget,
                spentAmount = spent,
                progress = progress.coerceIn(0f, 1f)
            )
        }

        BudgetsUiState(
            budgetsWithProgress = budgetsWithProgress,
            availableCategories = categories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetsUiState()
    )

    fun createBudget(category: String, amount: Double) {
        viewModelScope.launch {
            val newBudget = Budget(category = category, amount = amount)
            budgetRepository.insertBudget(newBudget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(budget)
        }
    }
}
