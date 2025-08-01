package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.Transaction
import com.example.cashflow.data.TransactionRepository
import com.example.cashflow.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Date

class AddEditTransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun setTransactionType(type: String) {
        _uiState.update { it.copy(type = type) }
    }

    fun setAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun setCategory(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun setDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun setDate(date: Long) {
        _uiState.update { it.copy(date = date) }
    }

    fun loadTransaction(transactionId: Int) {
        viewModelScope.launch {
 val transaction = transactionRepository.getTransactionById(transactionId).firstOrNull()
 if (transaction != null) {
                _uiState.update {
 it.copy(
 id = transaction.id,
 type = transaction.type,
 amount = transaction.amount.toString(),
 category = transaction.category,
 description = transaction.description,
 date = transaction.date,
            //             date = transaction.date,
            //             isEditing = true
            //         )
            //     }
            // }
        }
    }

    fun saveTransaction() {
        if (validateInput()) {
            viewModelScope.launch {
                val transaction = Transaction(
                    id = _uiState.value.id,
                    type = _uiState.value.type,
 amount = _uiState.value.amount.toDoubleOrNull() ?: 0.0, // Handle potential conversion errors
                    category = _uiState.value.category,
                    description = _uiState.value.description,
                    date = _uiState.value.date,
                    timestamp = System.currentTimeMillis()
                )
                if (_uiState.value.isEditing) {
                    transactionRepository.updateTransaction(transaction)
                } else {
                    transactionRepository.insertTransaction(transaction)
                }
            }
        }
    }

    private fun validateInput(): Boolean {
 return _uiState.value.amount.toDoubleOrNull() != null &&
 _uiState.value.category.isNotBlank() &&
 _uiState.value.description.isNotBlank()
    }

 companion object {
 val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
 @Suppress("UNCHECKED_CAST")
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
 return AddEditTransactionViewModel(
 AppContainer.transactionRepository
 ) as T
 }
 }
}

data class TransactionUiState(
    val id: Int = 0,
    val type: String = "Gasto",
    val amount: String = "",
    val category: String = "",
    val description: String = "",
    val date: Long = Date().time,
    val isEditing: Boolean = false
)