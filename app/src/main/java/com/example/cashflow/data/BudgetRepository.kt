package com.example.cashflow.data

import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    fun getAllBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllBudgets()
    }

    fun getBudgetByCategory(category: String): Flow<Budget?> {
        return budgetDao.getBudgetByCategory(category)
    }

    suspend fun insertBudget(budget: Budget) {
        budgetDao.insertBudget(budget)
    }

    suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }
}