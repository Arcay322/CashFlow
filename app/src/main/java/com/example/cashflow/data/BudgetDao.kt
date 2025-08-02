package com.example.cashflow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("SELECT * FROM budget")
    fun getAllBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budget WHERE category = :category LIMIT 1")
    fun getBudgetByCategory(category: String): Flow<Budget?>
}
