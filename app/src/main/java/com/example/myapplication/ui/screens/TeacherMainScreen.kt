package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherMainScreen(
    onLogout: () -> Unit,
    onSwitchRole: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("home") }
    var currentSubScreen by remember { mutableStateOf("home") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("MPAZ PRO 1.°", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = UtpPurpleSoft) {
                            Text("Docente", fontSize = 10.sp, color = UtpPurplePrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { currentSubScreen = "report" }) {
                        Icon(Icons.Default.Assessment, contentDescription = "Informe", tint = UtpTextPrimary)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir", tint = UtpTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UtpWarmBackground)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = UtpCardBackground) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = selectedTab == "home",
                    onClick = { selectedTab = "home"; currentSubScreen = "home" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpPurplePrimary, selectedTextColor = UtpPurplePrimary)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
                    label = { Text("Biblioteca") },
                    selected = selectedTab == "library",
                    onClick = { selectedTab = "library"; currentSubScreen = "library" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpPurplePrimary, selectedTextColor = UtpPurplePrimary)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text("Mi curso") },
                    selected = selectedTab == "course",
                    onClick = { selectedTab = "course"; currentSubScreen = "course" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpPurplePrimary, selectedTextColor = UtpPurplePrimary)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assessment, contentDescription = null) },
                    label = { Text("Informes") },
                    selected = selectedTab == "reports",
                    onClick = { selectedTab = "reports"; currentSubScreen = "report" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpPurplePrimary, selectedTextColor = UtpPurplePrimary)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(UtpWarmBackground)) {
            when (currentSubScreen) {
                "home" -> TeacherHomeScreen(
                    onNavigateTo = { route ->
                        when (route) {
                            "course_progress" -> currentSubScreen = "course"
                            "library" -> currentSubScreen = "library"
                            else -> currentSubScreen = "course"
                        }
                    }
                )
                "library" -> TeacherLibraryView(onAssign = { currentSubScreen = "assign" })
                "assign" -> TeacherAssignView(onAssigned = { currentSubScreen = "course" })
                "course" -> TeacherCourseProgressView(onSelectStudent = { currentSubScreen = "responses" })
                "responses" -> TeacherStudentResponsesView(onOpenReport = { currentSubScreen = "report" })
                "report" -> TeacherPedagogicalReportView(onValidate = {
                    Toast.makeText(context, "Informe validado correctamente. En proceso de conexión a BD", Toast.LENGTH_LONG).show()
                })
            }

            DbStatusBadge(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                statusText = "En proceso de conexión a BD",
                isVisible = true
            )
        }
    }
}

@Composable
fun TeacherLibraryView(onAssign: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Biblioteca autorizada", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text("1.° Básico • Contenidos revisados por UTP", fontSize = 12.sp, color = UtpTextSecondary)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Matemáticas - Contamos del 1 al 10", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Text("Autorizado por UTP • Actividades: 3 desafíos + Prueba por OA", fontSize = 12.sp, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onAssign,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Seleccionar OA de Matemática", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherAssignView(onAssigned: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Asignar objetivos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text("Curso: 1.° A - Configuración docente", fontSize = 12.sp, color = UtpTextSecondary)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Objetivo Seleccionado", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Matemática: Contar y representar cantidades del 1 al 10.", fontSize = 13.sp, color = UtpTextSecondary)
                    Divider()
                    Text("Disponibilidad de la prueba", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Desde: 22/10/2026 - 08:00\nHasta: 26/10/2026 - 18:00\nIntentos permitidos: 2", fontSize = 12.sp, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, "Prueba asignada. En proceso de conexión a BD", Toast.LENGTH_SHORT).show()
                            onAssigned()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Asignar y habilitar con estas reglas", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherCourseProgressView(onSelectStudent: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Progreso del curso", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text("1.° A - 4 estudiantes ficticios", fontSize = 12.sp, color = UtpTextSecondary)
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onSelectStudent),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Estudiante 01", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                        Surface(shape = RoundedCornerShape(10.dp), color = UtpBadgeGreenBg) {
                            Text("Finalizada", color = UtpBadgeGreenText, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                    Text("Perfil ficticio • OA Contemos del 1-10", fontSize = 12.sp, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("4/5 correctas • Intento 1/2", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                }
            }
        }
    }
}

@Composable
fun TeacherStudentResponsesView(onOpenReport: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Respuestas del estudiante", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text("Estudiante 01 - Perfil ficticio", fontSize = 12.sp, color = UtpTextSecondary)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("4 / 5 Correctas - 80%", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenReport,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Abrir informe reservado", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherPedagogicalReportView(onValidate: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Informe pedagógico", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text("Reservado • Solo docente autorizado", fontSize = 12.sp, color = UtpTextSecondary)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Estudiante 01 - Perfil ficticio", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Resultado: 80% (4 de 5 correctas, 1 incorrecta)", fontSize = 13.sp, color = UtpTextSecondary)
                    Divider()
                    Text("Observación pedagógica (Editable):", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Propondré contar 9 fichas, una a una, y contrastar el total con la tarjeta del número 9.", fontSize = 13.sp, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onValidate,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Validar informe", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { Toast.makeText(context, "Descargando borrador - En proceso de conexión a BD", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Descargar borrador (no validado)", color = UtpTextPrimary)
                    }
                }
            }
        }
    }
}
