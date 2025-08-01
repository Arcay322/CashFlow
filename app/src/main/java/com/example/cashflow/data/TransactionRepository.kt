package com.example.cashflow.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getRecentTransactions(limit: Int): Flow<List<Transaction>> = transactionDao.getRecentTransactions(limit)

    fun getExpenseByCategory(): Flow<List<TransactionDao.CategoryExpense>> = transactionDao.getExpenseByCategory()

    fun getTotalIncome(): Flow<Double> = transactionDao.getTotalIncome()

    fun getTotalExpense(): Flow<Double> = transactionDao.getTotalExpense()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
}
