package com.example.cashflow.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(private val budgetDao: BudgetDao) {

    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAllBudgets()

    fun getBudgetByCategory(category: String): Flow<Budget?> = budgetDao.getBudgetByCategory(category)

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
