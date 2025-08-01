package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.Category
import com.example.cashflow.data.CategoryRepository
import com.example.cashflow.data.SettingsRepository // Assuming you'll create this
import com.example.cashflow.data.TransactionRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository, // Assuming you'll create this
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _currentTheme = MutableStateFlow<String>("System") // Or a custom sealed class
    val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

    private val _exportStatus = MutableSharedFlow<ExportStatus>()
    val exportStatus: SharedFlow<ExportStatus> = _exportStatus.asSharedFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect {
                _categories.value = it // Update the StateFlow with the latest categories
            }
        }
        viewModelScope.launch {
            // Assuming SettingsRepository provides a Flow<String> for the theme
            settingsRepository.getTheme().collect {
                // Collect the theme and update the currentTheme StateFlow
                _currentTheme.value = it
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.insertCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.saveTheme(theme)
        }
    }

    fun exportTransactionsToCsv(exportPath: String) {
        viewModelScope.launch {
            _exportStatus.emit(ExportStatus.Loading)
            try { // Use a try-catch block to handle potential errors during file operations
                // Assuming TransactionRepository has a function to get all transactions once (not a Flow)
                val transactions = transactionRepository.getAllTransactionsOnce() // Need a non-Flow way to get all transactions
                // Logic to write transactions to a CSV file at exportPath
                // This would involve file I/O and formatting the transaction data
                _exportStatus.emit(ExportStatus.Success(exportPath))
            } catch (e: Exception) {
                _exportStatus.emit(ExportStatus.Error(e.localizedMessage ?: "Export failed"))
            }
        }
    }

    fun scheduleReminder(timeMillis: Long) {
        // Placeholder for reminder scheduling logic
        // Logic to schedule an alarm or notification
        // This would likely involve the AlarmManager or WorkManager
    }

    fun cancelReminder() {
        // Placeholder for reminder cancellation logic
        // Logic to cancel any scheduled reminders
    }

    sealed class ExportStatus {
        object Loading : ExportStatus()
        data class Success(val filePath: String) : ExportStatus()
        data class Error(val message: String) : ExportStatus()
    }
}