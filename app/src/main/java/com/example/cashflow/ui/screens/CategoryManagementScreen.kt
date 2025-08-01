package com.example.cashflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashflow.data.Category
import com.example.cashflow.ui.viewmodel.CategoryManagementViewModel

@Composable
fun CategoryManagementScreen(
    viewModel: CategoryManagementViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                categoryToEdit = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Categoría")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Gestionar Categorías", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        onEdit = {
                            categoryToEdit = it
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteCategory(it) }
                    )
                }
            }
        }

        if (showDialog) {
            CategoryEditDialog(
                category = categoryToEdit,
                onDismiss = { showDialog = false },
                onConfirm = { name, icon, type ->
                    if (categoryToEdit == null) {
                        viewModel.addCategory(name, icon, type)
                    } else {
                        viewModel.updateCategory(categoryToEdit!!.copy(name = name, icon = icon, type = type))
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aquí se podría mostrar el icono de la categoría
            // Icon( ... )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = category.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = category.type, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onEdit(category) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Categoría")
            }
            IconButton(onClick = { onDelete(category) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Categoría")
            }
        }
    }
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
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Categoría") }
                )
                Row {
                   Text("Tipo: ")
                   RadioButton(selected = type == "Gasto", onClick = { type = "Gasto" })
                   Text("Gasto")
                   Spacer(Modifier.width(8.dp))
                   RadioButton(selected = type == "Ingreso", onClick = { type = "Ingreso" })
                   Text("Ingreso")
                }
                // Aquí se podría añadir un selector de iconos
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, "", type) }, // Icono se deja vacío por ahora
                enabled = isFormValid
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
