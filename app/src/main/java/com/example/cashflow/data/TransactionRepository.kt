package com.example.cashflow.data

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    fun getTransactionsByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }

    fun getTransactionsByCategory(category: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(category)
    }

    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }

    fun searchTransactions(searchQuery: String): Flow<List<Transaction>> {
        return transactionDao.searchTransactions(searchQuery)
    }

    fun getTotalIncome(): Flow<Double?> {
        return transactionDao.getTotalIncome()
    }

    fun getTotalExpense(): Flow<Double?> {
        return transactionDao.getTotalExpense()
    }

    fun getRecentTransactions(limit: Int): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions(limit)
    }

    fun getExpenseDistributionByCategory(date: Long): Flow<Map<String, Double>> {
        return transactionDao.getExpenseDistributionByCategory(date)
    }

    fun getTotalExpenseByCategory(category: String): Flow<Double?> {
        return transactionDao.getTotalExpenseByCategory(category)
    }

    // You could add methods here for more complex queries or calculations if needed
    // For example, calculating the balance for a specific period
}