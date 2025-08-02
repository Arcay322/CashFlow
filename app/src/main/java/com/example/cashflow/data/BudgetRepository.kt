package com.example.cashflow.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(private val budgetDao: BudgetDao) {

    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAll()

    suspend fun insertBudget(budget: Budget) {
        budgetDao.insert(budget)
    }

    suspend fun deleteBudget(budget: Budget) {
        budgetDao.delete(budget)
    }
}
