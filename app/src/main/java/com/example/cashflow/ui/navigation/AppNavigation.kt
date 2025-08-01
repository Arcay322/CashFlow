package com.example.cashflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cashflow.ui.screens.*
import com.example.cashflow.ui.viewmodel.*

object Routes {
    const val DASHBOARD = "dashboard"
    const val HISTORY = "history"
    const val BUDGETS = "budgets"
    const val SETTINGS = "settings"
    const val ADD_TRANSACTION = "add_transaction"
    const val CATEGORY_MANAGEMENT = "category_management"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
        modifier = modifier
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(viewModel = hiltViewModel<DashboardViewModel>())
        }
        composable(Routes.HISTORY) {
            TransactionHistoryScreen(viewModel = hiltViewModel<TransactionHistoryViewModel>())
        }
        composable(Routes.BUDGETS) {
            BudgetsScreen(viewModel = hiltViewModel<BudgetsViewModel>())
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }
        composable(Routes.ADD_TRANSACTION) {
            AddEditTransactionScreen(navController = navController)
        }
        composable(Routes.CATEGORY_MANAGEMENT) {
            CategoryManagementScreen(viewModel = hiltViewModel<CategoryManagementViewModel>())
        }
    }
}
