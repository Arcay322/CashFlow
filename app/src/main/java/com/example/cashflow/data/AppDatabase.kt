package com.example.cashflow.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class, Budget::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
}
