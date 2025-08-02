package com.example.cashflow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type = 'Gasto' GROUP BY category")
    fun getExpensesByCategory(): Flow<List<CategoryExpense>>
    
    // --- MÉTODOS AÑADIDOS ---
    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Ingreso'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Gasto'")
    fun getTotalExpense(): Flow<Double?>
}

data class CategoryExpense(val category: String, val total: Double)
