package com.example.myapplication.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    onLogout: () -> Unit = {},
    onSwitchRole: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("home") }
    var currentSubScreen by remember { mutableStateOf("main") }

    Scaffold(
        topBar = {
            UtpTopHeaderBar(
                userRole = "ALUMNO",
                userName = "Camila Morales (1° A)",
                showBack = currentSubScreen != "main",
                onBack = { currentSubScreen = "main" },
                onLogout = onLogout
            )
        },
        bottomBar = {
            if (currentSubScreen == "main") {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = selectedTab == "home",
                        onClick = { selectedTab = "home" },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UtpRed,
                            selectedTextColor = UtpRed,
                            indicatorColor = UtpRed.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Book, contentDescription = "Contenido") },
                        label = { Text("Contenido") },
                        selected = selectedTab == "contenido",
                        onClick = { selectedTab = "contenido" },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UtpRed,
                            selectedTextColor = UtpRed,
                            indicatorColor = UtpRed.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Grade, contentDescription = "Mis Notas") },
                        label = { Text("Mis Notas") },
                        selected = selectedTab == "notas",
                        onClick = { selectedTab = "notas" },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UtpRed,
                            selectedTextColor = UtpRed,
                            indicatorColor = UtpRed.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Horario") },
                        label = { Text("Horario") },
                        selected = selectedTab == "horario",
                        onClick = { selectedTab = "horario" },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UtpRed,
                            selectedTextColor = UtpRed,
                            indicatorColor = UtpRed.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(UtpWarmBackground)
        ) {
            when {
                currentSubScreen == "quiz" -> {
                    StudentQuizScreen(
                        onBack = { currentSubScreen = "main" },
                        onFinishQuiz = { currentSubScreen = "main"; selectedTab = "home" }
                    )
                }
                currentSubScreen == "practice" -> {
                    StudentPracticeScreen(
                        onComplete = { currentSubScreen = "quiz" },
                        onBack = { currentSubScreen = "main" }
                    )
                }
                else -> {
                    when (selectedTab) {
                        "home" -> StudentHomeScreen(
                            onSelectAsignatura = { selectedTab = "contenido" },
                            onLogout = onLogout // <-- AÑADIDO
                        )
                        "contenido" -> StudentUnitDetailScreen(
                            onBack = { selectedTab = "home" },
                            onStartQuiz = { currentSubScreen = "quiz" },
                            onLogout = onLogout // <-- AÑADIDO
                        )
                        "notas" -> GradesScreen(
                            onBack = { selectedTab = "home" },
                            onLogout = onLogout // <-- AÑADIDO
                        )
                        "horario" -> ScheduleScreen(
                            onBack = { selectedTab = "home" },
                            onLogout = onLogout // <-- AÑADIDO
                        )
                    }
                }
            }

            DbStatusBadge(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                statusText = "En proceso de integración de base de datos",
                isVisible = true
            )
        }
    }
}