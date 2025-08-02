package com.example.cashflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cashflow.ui.navigation.AppNavigation
import com.example.cashflow.ui.navigation.Routes
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun CashFlowApp() {
    CashFlowTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate(Routes.ADD_TRANSACTION) }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Transacción")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.History,
        BottomNavItem.Budgets,
        BottomNavItem.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
    object Dashboard : BottomNavItem("Dashboard", Icons.Default.Dashboard, Routes.DASHBOARD)
    object History : BottomNavItem("Historial", Icons.Default.History, Routes.HISTORY)
    object Budgets : BottomNavItem("Presupuesto", Icons.Default.Assessment, Routes.BUDGETS)
    object Settings : BottomNavItem("Ajustes", Icons.Default.Settings, Routes.SETTINGS)
}
