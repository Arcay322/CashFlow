package com.example.cashflow.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashflow.data.Transaction
import com.example.cashflow.data.TransactionDao
import com.example.cashflow.ui.components.GlassmorphismCard
import com.example.cashflow.ui.viewmodel.DashboardViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BalanceCard(
                balance = uiState.balance,
                income = uiState.totalIncome,
                expense = uiState.totalExpense
            )
        }
        item {
            ExpenseChartCard(expenses = uiState.expenseDistribution)
        }
        item {
            RecentTransactionsCard(transactions = uiState.recentTransactions)
        }
    }
}

@Composable
private fun BalanceCard(balance: Double, income: Double, expense: Double) {
    GlassmorphismCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Balance Total",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ingresos", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = formatCurrency(income),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32) // Green
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Gastos", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = formatCurrency(expense),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC62828) // Red
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseChartCard(expenses: List<TransactionDao.CategoryExpense>) {
    GlassmorphismCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Distribución de Gastos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (expenses.isEmpty()) {
                Text(
                    text = "No hay gastos registrados este mes.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                SimplePieChart(data = expenses)
            }
        }
    }
}

@Composable
private fun RecentTransactionsCard(transactions: List<Transaction>) {
    GlassmorphismCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Transacciones Recientes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (transactions.isEmpty()){
                 Text(
                    text = "No hay transacciones recientes.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                transactions.forEach { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon placeholder
        // Icon(imageVector = Icons.Default.Category, contentDescription = transaction.category)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(text = transaction.category, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = formatCurrency(transaction.amount),
            color = if (transaction.type == "Ingreso") Color(0xFF2E7D32) else Color(0xFFC62828),
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun SimplePieChart(
    data: List<TransactionDao.CategoryExpense>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.total }
    val colors = remember { generateRandomColors(data.size) }
    
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(150.dp)) {
            var startAngle = 0f
            data.forEachIndexed { index, item ->
                val sweepAngle = (item.total / total).toFloat() * 360f
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(width = size.width, height = size.height)
                )
                startAngle += sweepAngle
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            data.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(colors[index]))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${item.category}: ${formatCurrency(item.total)}", fontSize = 14.sp)
                }
            }
        }
    }
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "ES")).format(amount)
}

fun generateRandomColors(count: Int): List<Color> {
    val colors = mutableListOf<Color>()
    for (i in 0 until count) {
        colors.add(
            Color(
                red = Random.nextFloat(),
                green = Random.nextFloat(),
                blue = Random.nextFloat(),
                alpha = 1f
            )
        )
    }
    return colors
}
