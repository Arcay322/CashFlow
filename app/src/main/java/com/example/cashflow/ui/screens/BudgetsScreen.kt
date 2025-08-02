package com.example.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashflow.data.Budget
import com.example.cashflow.data.Category
import com.example.cashflow.ui.components.GlassmorphismCard
import com.example.cashflow.ui.util.formatCurrency
import com.example.cashflow.ui.viewmodel.BudgetsViewModel
import com.example.cashflow.ui.viewmodel.BudgetWithProgress

@Composable
fun BudgetsScreen(viewModel: BudgetsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    // ... (El resto del composable con las correcciones de nombres)
}
// ... (El resto de los composables internos con las correcciones)
