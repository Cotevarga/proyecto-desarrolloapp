package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.GradesScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.ScheduleScreen
import com.example.myapplication.ui.screens.StudentMainScreen
import com.example.myapplication.ui.screens.TeacherMainScreen

// Unificación de todas las rutas de la aplicación
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object StudentMain : Screen("student_main")
    object TeacherMain : Screen("teacher_main")
    object JefaturaMain : Screen("jefatura_main")
    object Grades : Screen("grades")
    object Schedule : Screen("schedule")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 1. Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    navigateToRole(navController, role)
                }
            )
        }

        // 2. Pantalla Principal de Alumno
        composable(Screen.StudentMain.route) {
            StudentMainScreen(
                onLogout = { logout(navController) },
                onSwitchRole = { newRole -> navigateToRole(navController, newRole) },
                onNavigateToGrades = { navController.navigate(Screen.Grades.route) },
                onNavigateToSchedule = { navController.navigate(Screen.Schedule.route) }
            )
        }

        // 3. Pantalla Principal de Docente
        composable(Screen.TeacherMain.route) {
            TeacherMainScreen(
                onLogout = { logout(navController) },
                onSwitchRole = { newRole -> navigateToRole(navController, newRole) }
            )
        }

        // 4. Pantalla Principal de Jefatura UTP
        composable(Screen.JefaturaMain.route) {
            MainScreen(
                onLogout = { logout(navController) }
            )
        }

        // 5. Pantallas Secundarias
        composable(Screen.Grades.route) {
            GradesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Schedule.route) {
            ScheduleScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun navigateToRole(navController: NavHostController, role: String) {
    val targetRoute = when (role.uppercase()) {
        "ALUMNO" -> Screen.StudentMain.route
        "DOCENTE" -> Screen.TeacherMain.route
        else -> Screen.JefaturaMain.route
    }

    navController.navigate(targetRoute) {
        popUpTo(Screen.Login.route) { inclusive = true }
    }
}

private fun logout(navController: NavHostController) {
    navController.navigate(Screen.Login.route) {
        popUpTo(0) { inclusive = true }
    }
}