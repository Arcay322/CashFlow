package com.example.cashflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cashflow.ui.viewmodel.AddEditTransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddEditTransactionScreen(
    navController: NavController,
    transactionId: Int? = null,
    viewModel: AddEditTransactionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showCategoryDialog = remember { mutableStateOf(false) }
    val showDatePickerDialog = remember { mutableStateOf(false) }

    LaunchedEffect(transactionId) {
        if (transactionId != null && transactionId != -1) {
            viewModel.loadTransaction(transactionId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (transactionId == null || transactionId == -1) "Añadir Transacción" else "Editar Transacción") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type ToggleButton
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.onTypeChange("Ingreso") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (uiState.type == "Ingreso") MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                ) {
                    Text("Ingreso")
                }
                Button(
                    onClick = { viewModel.onTypeChange("Gasto") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (uiState.type == "Gasto") MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                ) {
                    Text("Gasto")
                }
            }

            // Amount Field
            OutlinedTextField(
                value = if (uiState.amount == 0.0) "" else formatCurrency(uiState.amount),
                onValueChange = {
                    try {
                        val amount = it.replace(",", "").replace(".", "").toDouble() / 100 // Simple currency formatting
                        viewModel.onAmountChange(amount)
                    } catch (e: NumberFormatException) {
                        // Handle invalid input if needed
                    }
                },
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selector
            OutlinedTextField(
                value = uiState.category,
                onValueChange = { }, // Not directly editable
                label = { Text("Categoría") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        // Replace with a category icon if available
                        Icons.Default.CalendarToday, // Placeholder icon
                        contentDescription = "Select Category",
                        modifier = Modifier.clickable { showCategoryDialog.value = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Description Field
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            // Date Field
            OutlinedTextField(
                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(uiState.date)),
                onValueChange = { }, // Not directly editable
                label = { Text("Fecha") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Select Date",
                        modifier = Modifier.clickable { showDatePickerDialog.value = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    if (transactionId == null || transactionId == -1) {
                        viewModel.saveTransaction()
                    } else {
                        viewModel.updateTransaction(transactionId)
                    }
                    navController.popBackStack() // Navigate back after saving
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (transactionId == null || transactionId == -1) "Guardar Transacción" else "Actualizar Transacción")
            }
        }

        // Category Selection Dialog
        if (showCategoryDialog.value) {
            CategorySelectionDialog(
                onCategorySelected = { category ->
                    viewModel.onCategoryChange(category)
                    showCategoryDialog.value = false
                },
                onDismiss = { showCategoryDialog.value = false }
            )
        }

        // Date Picker Dialog
        if (showDatePickerDialog.value) {
            DatePickerDialog(
                onDateSelected = { dateMillis ->
                    viewModel.onDateChange(dateMillis)
                    showDatePickerDialog.value = false
                },
                onDismiss = { showDatePickerDialog.value = false }
            )
        }
    }
}

@Composable
fun CategorySelectionDialog(
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // This is a placeholder. You would implement a grid of category icons here.
    // For simplicity, using a basic AlertDialog with sample categories.
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Categoría") },
        text = {
            Column {
                // Replace with your actual category grid implementation
                Text("Comida", modifier = Modifier.clickable { onCategorySelected("Comida") })
                Text("Transporte", modifier = Modifier.clickable { onCategorySelected("Transporte") })
                Text("Entretenimiento", modifier = Modifier.clickable { onCategorySelected("Entretenimiento") })
                // Add more categories here
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    // This is a placeholder. You would implement a proper date picker here.
    // Using a simple AlertDialog with a hardcoded date for demonstration.
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Fecha") },
        text = {
            // Implement a DatePicker Composable here
            // For now, just a placeholder
            Text("Implement a DatePicker here")
        },
        confirmButton = {
            Button(onClick = {
                // Replace with actual date from date picker
                val selectedDate = Calendar.getInstance()
                onDateSelected(selectedDate.timeInMillis)
                onDismiss()
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return format.format(amount)
}