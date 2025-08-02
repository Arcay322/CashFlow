package com.example.cashflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cashflow.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
        modifier = modifier
    ) {
        composable(Routes.DASHBOARD) { DashboardScreen() }
        composable(Routes.HISTORY) { TransactionHistoryScreen() }
        composable(Routes.BUDGETS) { BudgetsScreen() }
        composable(Routes.SETTINGS) { SettingsScreen(navController = navController) }
        composable(Routes.ADD_TRANSACTION) { AddEditTransactionScreen(navController = navController) }
        composable(Routes.CATEGORY_MANAGEMENT) { CategoryManagementScreen() }
    }
}
