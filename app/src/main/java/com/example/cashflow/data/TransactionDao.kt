package com.example.cashflow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE description LIKE :searchQuery ORDER BY date DESC")
    fun searchTransactions(searchQuery: String): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'Ingreso'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'Gasto'")
    fun getTotalExpense(): Flow<Double?>

    @Query("SELECT * FROM `transaction` ORDER BY date DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'Gasto' AND strftime('%Y-%m', date / 1000, 'unixepoch') = strftime('%Y-%m', :date / 1000, 'unixepoch') GROUP BY category")
    fun getExpenseDistributionByCategory(date: Long): Flow<Map<String, Double>>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'Gasto' AND category = :category")
    fun getTotalExpenseByCategory(category: String): Flow<Double?>
}