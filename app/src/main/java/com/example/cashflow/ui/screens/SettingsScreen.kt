package com.example.cashflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cashflow.ui.navigation.Routes

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))

        SettingsItem(
            icon = Icons.Default.Category,
            title = "Gestionar Categorías",
            onClick = { navController.navigate(Routes.CATEGORY_MANAGEMENT) }
        )
        
        Divider()
        
        SettingsItem(
            icon = Icons.Default.DarkMode,
            title = "Tema Oscuro",
            trailingContent = {
                var isChecked by remember { mutableStateOf(false) }
                Switch(checked = isChecked, onCheckedChange = { isChecked = it })
            }
        )
        
        Divider()

        SettingsItem(
            icon = Icons.Default.UploadFile,
            title = "Exportar a CSV",
            onClick = { /* Lógica de exportación aquí */ }
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Ir a $title", modifier = Modifier.size(16.dp))
        }
    }
}
