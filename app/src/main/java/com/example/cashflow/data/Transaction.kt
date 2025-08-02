package com.example.cashflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val amount: Double,
    val category: String,
    val description: String,
    val date: Long,
    val timestamp: Long // Campo añadido
)
