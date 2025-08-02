package com.example.cashflow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM `transaction` ORDER BY date DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>

    @Query("SELECT category, SUM(amount) as total FROM `transaction` WHERE type = 'Gasto' GROUP BY category")
    fun getExpenseByCategory(): Flow<List<CategoryExpense>>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'Ingreso'")
    fun getTotalIncome(): Flow<Double?> // Hacemos nullable por si no hay transacciones

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'Gasto'")
    fun getTotalExpense(): Flow<Double?> // Hacemos nullable por si no hay transacciones
}

// Clase de datos para el resultado de la consulta agrupada
// La sacamos fuera de la interfaz para que sea una clase de primer nivel
data class CategoryExpense(val category: String, val total: Double)
