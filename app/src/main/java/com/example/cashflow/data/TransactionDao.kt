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
}

// Clase auxiliar para la consulta de gastos agrupados.
data class CategoryExpense(val category: String, val total: Double)
