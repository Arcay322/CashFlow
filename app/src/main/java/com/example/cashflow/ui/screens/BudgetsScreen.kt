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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashflow.data.Budget
import com.example.cashflow.data.Category
import com.example.cashflow.ui.components.GlassmorphismCard
import com.example.cashflow.ui.viewmodel.BudgetsViewModel
import com.example.cashflow.ui.viewmodel.BudgetWithProgress

@Composable
fun BudgetsScreen(
    viewModel: BudgetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear Presupuesto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Presupuestos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
            
            if (uiState.budgetsWithProgress.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no has creado ningún presupuesto.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.budgetsWithProgress) { budgetWithProgress ->
                        BudgetCard(
                            item = budgetWithProgress,
                            onDelete = { viewModel.deleteBudget(it) }
                        )
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateBudgetDialog(
                availableCategories = uiState.availableCategories,
                onDismiss = { showCreateDialog = false },
                onCreate = { category, amount ->
                    viewModel.createBudget(category, amount)
                    showCreateDialog = false
                }
            )
        }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.budget.category,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDelete(item.budget) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Presupuesto")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${formatCurrency(item.spentAmount)} de ${formatCurrency(item.budget.amount)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { item.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                color = progressColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateBudgetDialog(
    availableCategories: List<Category>,
    onDismiss: () -> Unit,
    onCreate: (String, Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val isFormValid = amount.isNotBlank() && selectedCategory != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Presupuesto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        availableCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) amount = it },
                    label = { Text("Monto del Presupuesto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(selectedCategory!!.name, amount.toDouble()) },
                enabled = isFormValid
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
