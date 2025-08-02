package com.example.cashflow.ui.util

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "ES")).format(amount)
}
