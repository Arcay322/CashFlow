package com.example.cashflow.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashflow.data.CategoryExpense
import com.example.cashflow.data.Transaction
import com.example.cashflow.ui.components.GlassmorphismCard
import com.example.cashflow.ui.viewmodel.DashboardViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random

// ... (El resto de la pantalla y sus componentes internos)
