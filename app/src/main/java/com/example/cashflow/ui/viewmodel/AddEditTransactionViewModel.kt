package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.CategoryRepository
import com.example.cashflow.data.Transaction
import com.example.cashflow.data.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class AddTransactionUiState(
    val amount: String = "",
    val type: String = "Gasto",
    val category: String = "",
    val description: String = "",
    val date: Long = Date().time,
    val isSaving: Boolean = false,
    val transactionSaved: Boolean = false
)

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    val categoryRepository: CategoryRepository // Hacemos público para que la UI acceda a las categorías
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState = _uiState.asStateFlow()

    fun onAmountChange(newAmount: String) {
        // Permitir solo números y un punto decimal
        if (newAmount.matches(Regex("^\\d*\\.?\\d*\$"))) {
            _uiState.update { it.copy(amount = newAmount) }
        }
    }

    fun onTypeChange(newType: String) {
        _uiState.update { it.copy(type = newType) }
    }

    fun onCategoryChange(newCategory: String) {
        _uiState.update { it.copy(category = newCategory) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onDateChange(newDate: Long) {
        _uiState.update { it.copy(date = newDate) }
    }

    fun saveTransaction() {
        if (_uiState.value.amount.isBlank() || _uiState.value.category.isBlank()) {
            // Aquí se podría añadir lógica para mostrar un error al usuario
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            
            val newTransaction = Transaction(
                type = _uiState.value.type,
                amount = _uiState.value.amount.toDoubleOrNull() ?: 0.0,
                category = _uiState.value.category,
                description = _uiState.value.description.ifBlank { _uiState.value.category },
                date = _uiState.value.date,
                timestamp = Date().time
            )
            
            transactionRepository.insertTransaction(newTransaction)
            
            _uiState.update { it.copy(isSaving = false, transactionSaved = true) }
        }
    }
}
