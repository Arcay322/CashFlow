package com.example.cashflow.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cashflow.data.Category
import com.example.cashflow.ui.viewmodel.AddEditTransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddEditTransactionScreen(
    navController: NavController,
    viewModel: AddEditTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Si la transacción se guardó, volvemos a la pantalla anterior
    LaunchedEffect(uiState.transactionSaved) {
        if (uiState.transactionSaved) {
            navController.popBackStack()
        }
    }

    // Obtenemos las categorías del repositorio a través del ViewModel
    val categories by viewModel.categoryRepository.getAllCategories().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Añadir Transacción", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        TransactionTypeToggle(
            selectedType = uiState.type,
            onTypeChange = viewModel::onTypeChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = viewModel::onAmountChange,
            label = { Text("Monto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        CategorySelector(
            categories = categories,
            selectedCategory = uiState.category,
            onCategorySelected = viewModel::onCategoryChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Descripción (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        DatePicker(
            selectedDate = uiState.date,
            onDateSelected = viewModel::onDateChange
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = viewModel::saveTransaction,
            enabled = uiState.amount.isNotBlank() && uiState.category.isNotBlank() && !uiState.isSaving,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Guardar")
            }
        }
    }
}

@Composable
private fun TransactionTypeToggle(selectedType: String, onTypeChange: (String) -> Unit) {
    val types = listOf("Gasto", "Ingreso")
    
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        types.forEach { type ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.shape(positionInSet = types.indexOf(type), count = types.size),
                onClick = { onTypeChange(type) },
                selected = type == selectedType
            ) {
                Text(type)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelector(
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category.name)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DatePicker(selectedDate: Long, onDateSelected: (Long) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        timeInMillis = selectedDate
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var dateText by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val newCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            onDateSelected(newCalendar.timeInMillis)
            dateText = dateFormat.format(newCalendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha") },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

// Nota: Para que el DatePicker y el Dropdown funcionen correctamente,
// puede que necesites importar `androidx.compose.material.icons.filled.DateRange`
// y añadir las dependencias correctas si no estuvieran ya.
// En este caso, ya están incluidas en Material 3.
