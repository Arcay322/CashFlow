package com.example.cashflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.data.Category
import com.example.cashflow.ui.viewmodel.CategoryManagementViewModel

@Composable
fun CategoryManagementScreen(
    viewModel: CategoryManagementViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState(emptyList())
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestionar Categorías") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingCategory = null
                showAddEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Categoría")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onEditClick = {
                        editingCategory = it
                        showAddEditDialog = true
                    },
                    onDeleteClick = { viewModel.deleteCategory(it) }
                )
                Divider()
            }
        }

        if (showAddEditDialog) {
            AddEditCategoryDialog(
                categoryToEdit = editingCategory,
                onDismiss = { showAddEditDialog = false },
                onSave = { newCategory ->
                    if (editingCategory == null) {
                        viewModel.addCategory(newCategory)
                    } else {
                        viewModel.editCategory(newCategory.copy(id = editingCategory!!.id))
                    }
                    showAddEditDialog = false
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEditClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = category.name, fontWeight = FontWeight.Bold)
        Row {
            IconButton(onClick = { onEditClick(category) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Categoría")
            }
            IconButton(onClick = { onDeleteClick(category) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Categoría")
            }
        }
    }
}

@Composable
fun AddEditCategoryDialog(
    categoryToEdit: Category?,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    var icon by remember { mutableStateOf(categoryToEdit?.icon ?: "") } // Implement icon selection later
    var type by remember { mutableStateOf(categoryToEdit?.type ?: "Gasto") } // Default to Gasto

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (categoryToEdit == null) "Añadir Categoría" else "Editar Categoría") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // TODO: Implement icon selection UI
                Text("Icon: $icon (Selección de icono pendiente)")
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tipo:")
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        listOf("Gasto", "Ingreso").forEach { categoryType ->
                            Row(
                                modifier = Modifier
                                    .clickable { type = categoryType }
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = type == categoryType,
                                    onClick = { type = categoryType }
                                )
                                Text(categoryType)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(Category(name = name, icon = icon, type = type))
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}