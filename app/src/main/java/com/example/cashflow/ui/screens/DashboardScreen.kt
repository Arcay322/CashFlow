package com.example.cashflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Minus
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.data.Transaction
import com.example.cashflow.ui.theme.CashFlowTheme
import com.example.cashflow.ui.theme.GlassmorphismBackground
import com.example.cashflow.ui.theme.GlassmorphismBorder
import com.example.cashflow.ui.viewmodel.DashboardViewModel
import com.github.mikephil.charting.charts.PieChart
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val balance by viewModel.balance.collectAsState(initial = 0.0)
    val recentTransactions by viewModel.recentTransactions.collectAsState(initial = listOf())
    val expenseDistribution by viewModel.expenseDistribution.collectAsState(initial = emptyMap())

    // Convert map to list for easier processing in composable
    val expenseDistributionList = remember(expenseDistribution) {
        expenseDistribution.map { (category, amount) ->
            DashboardViewModel.ExpenseDistribution(category, amount ?: 0.0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Balance Card
        GlassmorphismCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Balance del Mes Actual", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(balance),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Expense Pie Chart Card
        GlassmorphismCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Distribución de Gastos por Categoría", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                if (expenseDistributionList.isNotEmpty()) {
                    // You would integrate your Pie Chart Composable here
                    // For now, let's just display the data as text
                    expenseDistributionList.forEach { (category, amount) ->
                        Text("$category: ${formatCurrency(amount)}")
                    }
                    // You would integrate a Pie Chart library here, e.g., from a third party or custom.
                    // Make it interactive to show category details on touch.
                } else {
                    Text("No hay datos de gastos para este mes.")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Transactions Card
        GlassmorphismCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Transacciones Recientes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                if (recentTransactions.isNotEmpty()) {
                    LazyColumn {
                        items(recentTransactions) { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }
                } else {
                    Text("No hay transacciones recientes.")
                }
            }
        }
    }
}

@Composable
fun GlassmorphismCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .blur(10.dp) // Apply blur for the glass effect
            .background(
                color = GlassmorphismBackground, // Semi-transparent background
                shape = RoundedCornerShape(16.dp)
            )
            .border( // Add a subtle border
                width = 1.dp,
                color = GlassmorphismBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Make card background transparent
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        content()
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for Category Icon - Replace with actual icon lookup
        Icon(
            imageVector = when (transaction.type) {
                "Ingreso" -> Icons.Default.Add
                "Gasto" -> Icons.Default.Minus
                else -> Icons.Default.Info
            },
            contentDescription = transaction.category,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.description, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = transaction.category, fontSize = 14.sp, color = Color.Gray)
        }
        Text(
            text = formatCurrency(transaction.amount),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.type == "Ingreso") Color.Green else Color.Red
        )
    }
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance()
    return format.format(amount)
}

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    CashFlowTheme {
        DashboardScreen()
    }
}