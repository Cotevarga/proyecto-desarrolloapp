package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.UtpRed

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object Schedule : BottomNavItem("horario", "Horario", Icons.Default.DateRange)
    object Grades : BottomNavItem("cursos", "Cursos", Icons.Default.Book)
    object Attendance : BottomNavItem("asistencia", "Asistencia", Icons.Default.CheckCircle)
    object Card : BottomNavItem("carne", "Carné", Icons.Default.QrCode)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Schedule,
        BottomNavItem.Grades,
        BottomNavItem.Attendance,
        BottomNavItem.Card
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UTP Móvil", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UtpRed),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = UtpRed,
                            selectedTextColor = UtpRed,
                            indicatorColor = UtpRed.copy(alpha = 0.1f)
                        ),
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(onNavigateToSection = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(BottomNavItem.Schedule.route) {
                ScheduleScreen()
            }
            composable(BottomNavItem.Grades.route) {
                GradesScreen()
            }
            composable(BottomNavItem.Attendance.route) {
                AttendanceScreen()
            }
            composable(BottomNavItem.Card.route) {
                DigitalCardScreen()
            }
        }
    }
}
