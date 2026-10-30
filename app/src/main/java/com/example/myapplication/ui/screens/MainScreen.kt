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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.myapplication.data.model.EstudianteAvance
import com.example.myapplication.ui.screens.jefatura.*
import com.example.myapplication.ui.screens.student.*
import com.example.myapplication.ui.screens.teacher.*

val UtpCelesteAccent = Color(0xFF0284C7)

sealed class NavTabItem(val route: String, val title: String, val icon: ImageVector) {
    // Jefatura UTP
    object UtpInicio : NavTabItem("utp_inicio", "Inicio", Icons.Default.Home)
    object UtpPanel : NavTabItem("admin_subjects", "Panel", Icons.Default.Dashboard)
    object UtpHistorial : NavTabItem("utp_historial", "Historial", Icons.Default.ListAlt)
    object UtpPublicar : NavTabItem("review_publish", "Publicar", Icons.Default.Publish)

    // Docente
    object TeacherHome : NavTabItem("teacher_home", "Home", Icons.Default.Home)
    object TeacherPermisos : NavTabItem("teacher_permissions", "Permisos", Icons.Default.AssignmentTurnedIn)
    object TeacherClases : NavTabItem("teacher_classes", "Clases", Icons.Default.Class)
    object TeacherAlumnos : NavTabItem("teacher_students", "Alumnos", Icons.Default.People)

    // Alumno
    object StudentInicio : NavTabItem("student_home", "Inicio", Icons.Default.Home)
    object StudentNotas : NavTabItem("student_grades", "Mis Notas", Icons.Default.Grade)
    object StudentHorario : NavTabItem("student_schedule", "Horario", Icons.Default.DateRange)
}

@Composable
fun MainScreen(
    userRole: String = "JEFATURA",
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    val tabs = remember(userRole) {
        when (userRole.uppercase()) {
            "ALUMNO" -> listOf(
                NavTabItem.StudentInicio,
                NavTabItem.StudentNotas,
                NavTabItem.StudentHorario
            )
            "DOCENTE" -> listOf(
                NavTabItem.TeacherHome,
                NavTabItem.TeacherPermisos,
                NavTabItem.TeacherClases,
                NavTabItem.TeacherAlumnos
            )
            else -> listOf(
                NavTabItem.UtpInicio,
                NavTabItem.UtpPanel,
                NavTabItem.UtpHistorial,
                NavTabItem.UtpPublicar
            )
        }
    }

    val startRoute = tabs.first().route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val esPantallaSecundaria = currentRoute?.startsWith("solicitudes_materia") == true ||
            currentRoute?.startsWith("student_report") == true ||
            currentRoute == "unit_editor" ||
            currentRoute == "jefatura_settings" ||
            currentRoute == "teacher_settings" ||
            currentRoute == "assign_objectives" ||
            currentRoute == "student_search" ||
            currentRoute == "teacher_request"

    Scaffold(
        bottomBar = {
            if (!esPantallaSecundaria) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    tabs.forEach { tab ->
                        val isSelected = currentRoute == tab.route

                        NavigationBarItem(
                            icon = { Icon(tab.icon, contentDescription = tab.title) },
                            label = { Text(tab.title) },
                            selected = isSelected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = UtpCelesteAccent,
                                selectedTextColor = UtpCelesteAccent,
                                indicatorColor = UtpCelesteAccent.copy(alpha = 0.15f)
                            ),
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = (tab.route != startRoute)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- JEFATURA UTP ---
            composable(NavTabItem.UtpInicio.route) {
                JefaturaHomeScreen(
                    onNavigateTo = { route -> navController.navigate(route) },
                    onAccountSettings = { navController.navigate("jefatura_settings") },
                    onLogout = onLogout
                )
            }

            composable(NavTabItem.UtpPanel.route) {
                AdminSubjectsScreen(
                    onBack = { navController.popBackStack() },
                    onSelectSubject = { subjectName ->
                        navController.navigate("solicitudes_materia/$subjectName")
                    },
                    onAccountSettings = { navController.navigate("jefatura_settings") },
                    onLogout = onLogout
                )
            }

            composable(NavTabItem.UtpHistorial.route) {
                HistorialCambiosScreen(
                    onAccountSettings = { navController.navigate("jefatura_settings") },
                    onLogout = onLogout
                )
            }

            composable(NavTabItem.UtpPublicar.route) {
                ReviewPublishScreen(
                    onBack = { navController.popBackStack() },
                    onEditUnit = { navController.navigate("unit_editor") },
                    onAccountSettings = { navController.navigate("jefatura_settings") },
                    onLogout = onLogout
                )
            }

            // --- VISTAS SECUNDARIAS JEFATURA ---
            composable("unit_editor") {
                UnitEditorScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("jefatura_settings") {
                JefaturaSettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }

            composable(
                route = "solicitudes_materia/{subjectName}",
                arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
            ) { backStackEntry ->
                val subjectName = backStackEntry.arguments?.getString("subjectName") ?: "Lenguaje"
                SolicitudesMateriaScreen(
                    subjectName = subjectName,
                    onBack = { navController.popBackStack() },
                    onAccountSettings = { navController.navigate("jefatura_settings") },
                    onLogout = onLogout
                )
            }

            // --- DOCENTE ---
            composable(NavTabItem.TeacherHome.route) {
                TeacherHomeScreen(
                    onNavigateTo = { route -> navController.navigate(route) },
                    onAccountSettings = { navController.navigate("teacher_settings") },
                    onLogout = onLogout
                )
            }

            composable(NavTabItem.TeacherPermisos.route) {
                TeacherPermissionsScreen(
                    onAccountSettings = { navController.navigate("teacher_settings") },
                    onLogout = onLogout
                )
            }

            composable(NavTabItem.TeacherClases.route) {
                TeacherLibraryScreen(
                    onBack = { navController.popBackStack() },
                    onSelectObjective = { navController.navigate("assign_objectives") },
                    onRequestPermission = { navController.navigate("teacher_request") },
                    onAccountSettings = { navController.navigate("teacher_settings") },
                    onLogout = onLogout
                )
            }

            composable(NavTabItem.TeacherAlumnos.route) {
                CourseProgressScreen(
                    onBack = { navController.popBackStack() },
                    onSelectStudentReport = { est: EstudianteAvance ->
                        navController.navigate("student_report/${est.nombre}")
                    },
                    onAccountSettings = { navController.navigate("teacher_settings") },
                    onLogout = onLogout
                )
            }

            // --- VISTAS SECUNDARIAS DOCENTE ---
            composable("teacher_settings") {
                TeacherSettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }

            composable("assign_objectives") {
                AssignObjectivesScreen(
                    onBack = { navController.popBackStack() },
                    onAssignedSuccess = { navController.navigate(NavTabItem.TeacherAlumnos.route) },
                    onLogout = onLogout
                )
            }

            composable("teacher_request") {
                TeacherRequestScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "student_report/{studentName}",
                arguments = listOf(navArgument("studentName") { type = NavType.StringType })
            ) { backStackEntry ->
                val studentName = backStackEntry.arguments?.getString("studentName") ?: "Lucas Morales"
                StudentReportScreen(
                    studentName = studentName,
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }

            // --- ALUMNO ---
            composable(NavTabItem.StudentInicio.route) {
                HomeScreen(onNavigateToSection = { route -> navController.navigate(route) })
            }

            composable(NavTabItem.StudentNotas.route) {
                GradesScreen(onBack = { navController.popBackStack() })
            }

            composable(NavTabItem.StudentHorario.route) {
                ScheduleScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}