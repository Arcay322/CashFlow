package com.example.cashflow.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert
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