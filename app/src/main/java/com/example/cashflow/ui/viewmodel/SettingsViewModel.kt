package com.example.cashflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cashflow.data.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository // Ejemplo de dependencia
) : ViewModel() {
    // El resto de la lógica del ViewModel
}
