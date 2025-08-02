package com.example.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashflow.data.Category
import com.example.cashflow.ui.viewmodel.CategoryManagementViewModel

@Composable
fun CategoryManagementScreen(viewModel: CategoryManagementViewModel = hiltViewModel()) {
    // ... (Contenido principal sin cambios)
}

@Composable
private fun CategoryEditDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var type by remember { mutableStateOf(category?.type ?: "Gasto") }
    val isFormValid = name.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Nueva Categoría" else "Editar Categoría") },
        text = {
            // ... (Contenido sin cambios)
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, "", type) }, // <-- Icono pasado como string vacío
                enabled = isFormValid
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
// ... (El resto del archivo sin cambios)
