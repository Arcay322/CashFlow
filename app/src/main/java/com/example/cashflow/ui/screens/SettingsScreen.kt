package com.example.cashflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.ui.viewmodel.SettingsViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    onNavigateToManageCategories: () -> Unit = {},
    onNavigateToExportData: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ajustes") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SettingsItem(
                icon = Icons.Default.Category,
                title = "Gestionar Categorías",
                onClick = onNavigateToManageCategories
            )
            Divider()
            AppearanceSetting(settingsViewModel)
            Divider()
            SettingsItem(
                icon = Icons.Default.CloudDownload,
                title = "Exportar Datos",
                onClick = onNavigateToExportData
            )
            Divider()
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Configurar Recordatorio Diario",
                onClick = onNavigateToReminders
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.h6)
        }
        Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun AppearanceSetting(settingsViewModel: SettingsViewModel) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.BrightnessLow else Icons.Default.BrightnessHigh,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Apariencia", style = MaterialTheme.typography.h6)
        }
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { settingsViewModel.setDarkTheme(it) }
        )
    }
}

@Composable
fun DailyReminderSetting(settingsViewModel: SettingsViewModel) {
    val dailyReminderTime by settingsViewModel.dailyReminderTime.collectAsState()
    val isDailyReminderEnabled by settingsViewModel.isDailyReminderEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Recordatorio Diario", style = MaterialTheme.typography.h6)
            }
            Switch(
                checked = isDailyReminderEnabled,
                onCheckedChange = { settingsViewModel.setDailyReminderEnabled(it) }
            )
        }

        if (isDailyReminderEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Hora del recordatorio:")
                // This is a placeholder. In a real app, you would use a TimePicker dialog.
                Button(onClick = { /* Open TimePicker */ }) {
                    Text(
                        text = dailyReminderTime?.let {
                            LocalTime.ofSecondOfDay(it).format(DateTimeFormatter.ofPattern("HH:mm"))
                        } ?: "Seleccionar hora"
                    )
                }
            }
        }
    }
}

// Placeholder composables for navigation destinations - these would be separate files/screens
@Composable
fun ManageCategoriesScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Gestionar Categorías") }) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Manage Categories UI goes here")
        }
    }
}

@Composable
fun ExportDataScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Exportar Datos") }) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Export Data UI goes here")
        }
    }
}

// Reminders screen might be integrated into settings or a separate screen depending on complexity.