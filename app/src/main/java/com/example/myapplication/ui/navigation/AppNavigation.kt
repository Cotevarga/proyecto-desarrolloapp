package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.student.GradesScreen
import com.example.myapplication.ui.screens.auth.LoginScreen
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.student.ScheduleScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
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
        // 1. Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    navController.navigate("${Screen.Main.route}/$role") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Pantalla Principal Adaptable al Rol
        composable(
            route = "${Screen.Main.route}/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "JEFATURA"
            MainScreen(
                userRole = role,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 3. Vistas adicionales independientes
        composable(Screen.Grades.route) {
            GradesScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Schedule.route) {
            ScheduleScreen(onBack = { navController.popBackStack() })
        }
    }
}