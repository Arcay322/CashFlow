package com.example.cashflow.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAll()

    fun getExpensesByCategory(): Flow<List<CategoryExpense>> = transactionDao.getExpensesByCategory()

    // --- MÉTODOS AÑADIDOS ---
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>> = transactionDao.getRecentTransactions(limit)

    fun getTotalIncome(): Flow<Double> = transactionDao.getTotalIncome().map { it ?: 0.0 }

    fun getTotalExpense(): Flow<Double> = transactionDao.getTotalExpense().map { it ?: 0.0 }
    
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insert(transaction)
    }
}
