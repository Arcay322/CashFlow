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
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear Presupuesto")
            }
        }
    ) { padding ->
        // ... (Contenido principal sin cambios, ahora compilará)
    }
}

@Composable
private fun BudgetCard(item: BudgetWithProgress, onDelete: (Budget) -> Unit) {
    val progressColor = when {
        item.progress > 0.9f -> MaterialTheme.colorScheme.error
        item.progress > 0.7f -> Color(0xFFFFA000) // Amber
        else -> MaterialTheme.colorScheme.primary
    }

    GlassmorphismCard {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Contenido sin cambios)
            Text(text = "${formatCurrency(item.spentAmount)} de ${formatCurrency(item.budget.amount)}")
            LinearProgressIndicator(
                progress = item.progress, // <-- LLAMADA CORREGIDA
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(MaterialTheme.shapes.small),
                color = progressColor
            )
        }
    }
}
// ... (El resto del archivo sin cambios)
