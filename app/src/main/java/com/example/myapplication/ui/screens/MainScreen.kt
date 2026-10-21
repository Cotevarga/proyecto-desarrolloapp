package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import com.example.myapplication.ui.theme.UtpOrangeHighlight
import com.example.myapplication.ui.theme.UtpPurplePrimary

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object Docente : BottomNavItem("teacher_home", "Docente", Icons.Default.Person)
    object Panel : BottomNavItem("admin_subjects", "Panel", Icons.Default.Dashboard)
    object Biblioteca : BottomNavItem("library", "Biblioteca", Icons.AutoMirrored.Filled.MenuBook)
    object Publicacion : BottomNavItem("review_publish", "Publicar", Icons.Default.Publish)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Docente,
        BottomNavItem.Panel,
        BottomNavItem.Biblioteca,
        BottomNavItem.Publicacion
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portal UTP Academic", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UtpOrangeHighlight),
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
                            selectedIconColor = UtpOrangeHighlight,
                            selectedTextColor = UtpOrangeHighlight,
                            indicatorColor = UtpOrangeHighlight.copy(alpha = 0.1f)
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
                JefaturaHomeScreen(onNavigateTo = { route -> navController.navigate(route) })
            }
            composable(BottomNavItem.Docente.route) {
                TeacherHomeScreen(onNavigateTo = { route -> navController.navigate(route) })
            }
            composable(BottomNavItem.Panel.route) {
                AdminSubjectsScreen(
                    onBack = { navController.popBackStack() },
                    onSelectSubject = { navController.navigate("unit_editor") }
                )
            }
            composable(BottomNavItem.Biblioteca.route) {
                TeacherLibraryScreen(
                    onBack = { navController.popBackStack() },
                    onSelectObjective = { navController.navigate("assign_objectives") }
                )
            }
            composable(BottomNavItem.Publicacion.route) {
                ReviewPublishScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("admin_subjects") {
                AdminSubjectsScreen(
                    onBack = { navController.popBackStack() },
                    onSelectSubject = { navController.navigate("unit_editor") }
                )
            }
            composable("teacher_home") {
                TeacherHomeScreen(onNavigateTo = { route -> navController.navigate(route) })
            }
            composable("library") {
                TeacherLibraryScreen(
                    onBack = { navController.popBackStack() },
                    onSelectObjective = { navController.navigate("assign_objectives") }
                )
            }
            composable("assign_objectives") {
                AssignObjectivesScreen(
                    onBack = { navController.popBackStack() },
                    onAssignedSuccess = { navController.navigate("course_progress") }
                )
            }
            composable("course_progress") {
                CourseProgressScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("unit_editor") {
                UnitEditorScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("review_publish") {
                ReviewPublishScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("activity_detail") {
                ActivityDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
