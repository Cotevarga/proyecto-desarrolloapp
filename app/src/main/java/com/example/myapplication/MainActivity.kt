package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.StudentMainScreen
import com.example.myapplication.ui.screens.TeacherMainScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var currentRole by remember { mutableStateOf("JEFATURA") }

                    if (isLoggedIn) {
                        when (currentRole) {
                            "ALUMNO" -> StudentMainScreen(
                                onLogout = { isLoggedIn = false },
                                onSwitchRole = { newRole -> currentRole = newRole }
                            )
                            "DOCENTE" -> TeacherMainScreen(
                                onLogout = { isLoggedIn = false },
                                onSwitchRole = { newRole -> currentRole = newRole }
                            )
                            else -> MainScreen(
                                onLogout = { isLoggedIn = false }
                            )
                        }
                    } else {
                        LoginScreen(
                            onLoginSuccess = { role ->
                                currentRole = role
                                isLoggedIn = true
                            }
                        )
                    }
                }
            }
        }
    }
}
