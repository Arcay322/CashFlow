package com.example.cashflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cashflow.ui.theme.CashFlowTheme
import com.example.cashflow.ui.view.budgets.BudgetsScreen
import com.example.cashflow.ui.view.dashboard.DashboardScreen
import com.example.cashflow.ui.view.history.TransactionHistoryScreen
import com.example.cashflow.ui.view.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CashFlowTheme {
                CashFlowApp()
            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Dashboard : Screen("dashboard", Icons.Default.Home, "Dashboard")
    object History : Screen("history", Icons.Default.History, "Historial")
    object Budgets : Screen("budgets", Icons.Default.AccountBalanceWallet, "Presupuestos")
    object Settings : Screen("settings", Icons.Default.Settings, "Ajustes")
    object AddEditTransaction : Screen("addEditTransaction", Icons.Default.Add, "Añadir Transacción")
}

@Composable
fun CashFlowApp() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Dashboard,
        Screen.History,
        Screen.Budgets,
        Screen.Settings,
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = items) {
                // Implement glassmorphism effect
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .blur(radius = 10.dp) // Apply blur effect
                    .background(
                        color = Color.White.copy(alpha = 0.1f), // Semi-transparent background
                        shape = CircleShape // Circular shape for the bar
                    )
                    .clip(CircleShape) // Clip content to the circular shape
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditTransaction.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, "Añadir Transacción")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen()
            }
            composable(Screen.History.route) {
                TransactionHistoryScreen()
            }
            composable(Screen.Budgets.route) {
                BudgetsScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.AddEditTransaction.route) {
                // TODO: Implement AddEditTransactionScreen
                Text("Add/Edit Transaction Screen")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: androidx.navigation.NavHostController,
    items: List<Screen>,
    modifier: @Composable (Modifier) -> Modifier
) {
    NavigationBar(
        modifier = modifier(Modifier),
        containerColor = Color.Transparent, // Make the container transparent for glassmorphism
        tonalElevation = 0.dp // Remove tonal elevation
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val targetColor = if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f)
            val animatedColor by animateColorAsState(
                targetValue = targetColor,
                animationSpec = tween(300)
            )
            val targetIconSize = if (selected) 32.dp else 24.dp
            val animatedIconSize by animateDpAsState(
                targetValue = targetIconSize,
                animationSpec = tween(300)
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        tint = animatedColor,
                        modifier = Modifier.size(animatedIconSize)
                    )
                },
                label = {
                    Text(
                        screen.label,
                        color = animatedColor,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // Make indicator transparent
                )
            )
        }
    }
}


/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashflow.ui.theme.CashFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CashFlowTheme {
        Greeting("Android")
    }
}
*/


/*
package com.example.cashflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashflow.ui.theme.CashFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CashFlowTheme {
        Greeting("Android")
    }
}