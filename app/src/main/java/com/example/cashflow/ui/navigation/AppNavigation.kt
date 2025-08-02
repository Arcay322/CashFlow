package com.example.cashflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cashflow.ui.screens.AddEditTransactionScreen
import com.example.cashflow.ui.screens.BudgetsScreen
import com.example.cashflow.ui.screens.CategoryManagementScreen
import com.example.cashflow.ui.screens.DashboardScreen
import com.example.cashflow.ui.screens.SettingsScreen
import com.example.cashflow.ui.screens.TransactionHistoryScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
        modifier = modifier
    ) {
        composable(Routes.DASHBOARD) { DashboardScreen(hiltViewModel()) }
        composable(Routes.HISTORY) { TransactionHistoryScreen(hiltViewModel()) }
        composable(Routes.BUDGETS) { BudgetsScreen(hiltViewModel()) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
        composable(Routes.ADD_TRANSACTION) { AddEditTransactionScreen(navController) }
        composable(Routes.CATEGORY_MANAGEMENT) { CategoryManagementScreen(hiltViewModel()) }
    }
}
