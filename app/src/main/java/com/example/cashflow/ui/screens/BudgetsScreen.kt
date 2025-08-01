package com.example.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.ui.theme.GlassmorphismCard
import com.example.cashflow.ui.viewmodel.BudgetsViewModel

@Composable
fun BudgetsScreen(budgetsViewModel: BudgetsViewModel = viewModel()) {
    val budgetsWithSpending by budgetsViewModel.budgetsWithSpending.collectAsState(initial = emptyList())
    val overallBudgetProgress by budgetsViewModel.overallBudgetProgress.collectAsState(initial = 0f)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // TODO: Navigate to or show dialog for creating a new budget
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Presupuesto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OverallBudgetProgress(progress = overallBudgetProgress)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(budgetsWithSpending) { budget ->
                    BudgetItem(budget = budget) {
                        // TODO: Handle budget deletion
                    }
                }
            }
        }
    }
}

@Composable
fun OverallBudgetProgress(progress: Float) {
    GlassmorphismCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Progreso Total de Presupuestos", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = when {
                    progress > 0.9f -> Color.Red
                    progress > 0.7f -> Color.Orange
                    else -> MaterialTheme.colors.primary
                },
                backgroundColor = MaterialTheme.colors.surface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Gastado ${String.format("%.2f", progress * 100)}%", // This calculation might need adjustment based on total budget vs total spending
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun BudgetItem(budget: BudgetsViewModel.BudgetWithSpending, onDeleteClick: () -> Unit) {
    GlassmorphismCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(budget.category, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = budget.spending / budget.limit.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = when {
                        budget.spending > budget.limit -> Color.Red
                        budget.spending > budget.limit * 0.9f -> Color.Orange
                        else -> MaterialTheme.colors.primary
                    },
                    backgroundColor = MaterialTheme.colors.surface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Gastado ${String.format("%.2f", budget.spending)} de ${String.format("%.2f", budget.limit)}",
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(onClick = onDeleteClick) {
                // TODO: Use a delete icon
                Text("X") // Placeholder
            }
        }
    }
}

// TODO: Create composable for CreateBudgetDialog or screen
// TODO: Create composable for DeleteBudgetConfirmationDialog