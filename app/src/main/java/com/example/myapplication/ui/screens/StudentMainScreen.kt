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
fun StudentMainScreen(
    onLogout: () -> Unit,
    onSwitchRole: (String) -> Unit,
    onNavigateToGrades: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("inicio") }
    var currentSubScreen by remember { mutableStateOf("home") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("MPAZ PRO 1.°", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = UtpOrangeSoft) {
                            Text("Estudiante", fontSize = 10.sp, color = UtpOrangeHighlight, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { currentSubScreen = "settings" }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = UtpTextPrimary)
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
                    selected = selectedTab == "inicio",
                    onClick = { selectedTab = "inicio"; currentSubScreen = "home" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpOrangeHighlight, selectedTextColor = UtpOrangeHighlight)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = null) },
                    label = { Text("Mi unidad") },
                    selected = selectedTab == "unidad",
                    onClick = { selectedTab = "unidad"; currentSubScreen = "unit" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpOrangeHighlight, selectedTextColor = UtpOrangeHighlight)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    label = { Text("Practicar") },
                    selected = selectedTab == "practicar",
                    onClick = { selectedTab = "practicar"; currentSubScreen = "practice" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpOrangeHighlight, selectedTextColor = UtpOrangeHighlight)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Buscar") },
                    selected = selectedTab == "buscar",
                    onClick = { selectedTab = "buscar"; currentSubScreen = "search" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpOrangeHighlight, selectedTextColor = UtpOrangeHighlight)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") },
                    selected = selectedTab == "perfil",
                    onClick = { selectedTab = "perfil"; currentSubScreen = "settings" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = UtpOrangeHighlight, selectedTextColor = UtpOrangeHighlight)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(UtpWarmBackground)) {
            when (currentSubScreen) {
                "home" -> StudentHomeScreen(
                    onOpenUnit = { currentSubScreen = "unit" },
                    onOpenPractice = { currentSubScreen = "practice" },
                    onSwitchProfile = { role -> onSwitchRole(role) },
                    onNavigateToGrades = onNavigateToGrades,
                    onNavigateToSchedule = onNavigateToSchedule
                )
                "unit" -> StudentUnitScreen(
                    onStartPractice = { currentSubScreen = "practice" },
                    onStartQuiz = { currentSubScreen = "quiz" }
                )
                "practice" -> StudentPracticeScreen(
                    onComplete = { currentSubScreen = "quiz" },
                    onBack = { currentSubScreen = "home" }
                )
                "quiz" -> StudentQuizScreen(
                    onSubmit = { currentSubScreen = "results" },
                    onBack = { currentSubScreen = "unit" }
                )
                "results" -> StudentResultsScreen(
                    onRetry = { currentSubScreen = "quiz" },
                    onBack = { currentSubScreen = "home" }
                )
                "search" -> StudentSearchScreen()
                "settings" -> SettingsScreen(onBack = { currentSubScreen = "home" })
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
fun StudentHomeScreen(
    onOpenUnit: () -> Unit,
    onOpenPractice: () -> Unit,
    onSwitchProfile: (String) -> Unit,
    onNavigateToGrades: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {}
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("PERFIL ACTIVO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                        Surface(shape = RoundedCornerShape(12.dp), color = UtpOrangeSoft) {
                            Text("Alumno", color = UtpOrangeHighlight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = UtpOrangeSoft) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = UtpOrangeHighlight)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Alberto Gómez", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Text("1° Básico • Escuela Escritoria Marcela Paz", fontSize = 12.sp, color = UtpTextSecondary)
                            Text("8 materias • 3 tareas pendientes", fontSize = 11.sp, color = UtpOrangeHighlight, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Accesos rápidos a Notas y Horarios
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onNavigateToGrades,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Grade, contentDescription = null, tint = UtpOrangeHighlight)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Notas", fontSize = 12.sp, color = UtpTextPrimary)
                }

                OutlinedButton(
                    onClick = onNavigateToSchedule,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = UtpOrangeHighlight)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Horario", fontSize = 12.sp, color = UtpTextPrimary)
                }
            }
        }

        // Materias
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Materias", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                TextButton(onClick = onOpenUnit) { Text("Ver todas", fontSize = 12.sp, color = UtpOrangeHighlight) }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SubjectMiniCard("Matemáticas", "8:30 • Sala 1B", Modifier.weight(1f), onClick = onOpenUnit)
                SubjectMiniCard("Química", "10:15 • Lab 2", Modifier.weight(1f), onClick = onOpenUnit)
                SubjectMiniCard("Lenguaje", "11:45 • Sala 1B", Modifier.weight(1f), onClick = onOpenUnit)
            }
        }

        // Tareas pendientes
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tareas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text("3 pendientes", fontSize = 12.sp, color = UtpOrangeHighlight, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskItemCard("Ejercicios de derivadas", "Matemáticas • Entrega hoy", "Hoy") {
                    Toast.makeText(context, "En proceso de conexión a BD", Toast.LENGTH_SHORT).show()
                }
                TaskItemCard("Resumen de reacciones", "Química • Entrega mañana", "Mañana") {
                    Toast.makeText(context, "En proceso de conexión a BD", Toast.LENGTH_SHORT).show()
                }
                TaskItemCard("Lectura de Cien años", "Literatura • Capítulo 3", "Semana") {
                    Toast.makeText(context, "En proceso de conexión a BD", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Progreso académico
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Progreso académico", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                        Text("84%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpOrangeHighlight)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        ProgressIndicatorItem("Asistencia", "92%")
                        ProgressIndicatorItem("Tareas", "88%")
                        ProgressIndicatorItem("Evaluaciones", "76%")
                    }
                }
            }
        }

        // Navegación educativa (Switch profiles)
        item {
            Text("Navegación educativa", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileSwitchCard("Alumno", "Activa", true, Modifier.weight(1f)) {}
                ProfileSwitchCard("Profesor", "Disponible", false, Modifier.weight(1f)) { onSwitchProfile("DOCENTE") }
                ProfileSwitchCard("Jefatura", "Disponible", false, Modifier.weight(1f)) { onSwitchProfile("JEFATURA") }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun SubjectMiniCard(name: String, time: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(Icons.Default.Book, contentDescription = null, tint = UtpOrangeHighlight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text(time, fontSize = 10.sp, color = UtpTextSecondary)
        }
    }
}

@Composable
fun TaskItemCard(title: String, subtitle: String, badge: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(8.dp), color = UtpOrangeSoft, modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Assignment, contentDescription = null, tint = UtpOrangeHighlight, modifier = Modifier.size(18.sp.value.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(subtitle, fontSize = 11.sp, color = UtpTextSecondary)
            }
            Surface(shape = RoundedCornerShape(10.dp), color = UtpOrangeSoft) {
                Text(badge, color = UtpOrangeHighlight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }
    }
}

@Composable
fun ProgressIndicatorItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = UtpTextSecondary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
    }
}

@Composable
fun ProfileSwitchCard(role: String, status: String, isActive: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isActive) UtpOrangeSoft else UtpCardBackground)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(Icons.Default.Person, contentDescription = null, tint = if (isActive) UtpOrangeHighlight else UtpTextSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(role, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text(status, fontSize = 11.sp, color = if (isActive) UtpOrangeHighlight else UtpTextSecondary)
        }
    }
}

@Composable
fun StudentUnitScreen(
    onStartPractice: () -> Unit,
    onStartQuiz: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Mi unidad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text("Matemática • 1.° Básico", fontSize = 12.sp, color = UtpTextSecondary)
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(shape = RoundedCornerShape(8.dp), color = UtpOrangeSoft) {
                        Text("Asignada por tu docente", fontSize = 10.sp, color = UtpOrangeHighlight, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Contamos del 1 al 10", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Text("Mi objetivo: contar y mostrar cantidades del 1 al 10.", fontSize = 13.sp, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { Toast.makeText(context, "Reproduciendo audio - En proceso de conexión a BD", Toast.LENGTH_SHORT).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpOrangeSoft),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = UtpOrangeHighlight)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Escuchar", color = UtpOrangeHighlight, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text("Tu camino", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PathStepCard("1. Aprender", "Mira y escucha el ejemplo", true, onStartPractice)
                PathStepCard("2. Practicar", "Actividades paso a paso", true, onStartPractice)
                PathStepCard("3. Desafíos", "3 de 3 completos", true, onStartPractice)
                PathStepCard("4. Prueba", "Completa el último desafío", false, onStartQuiz)
            }
        }

        item {
            Button(
                onClick = onStartQuiz,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UtpOrangeHighlight)
            ) {
                Text("Seguir con mi desafío", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun PathStepCard(title: String, subtitle: String, isDone: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (isDone) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = null,
                tint = if (isDone) UtpBadgeGreenText else UtpOrangeHighlight
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(subtitle, fontSize = 11.sp, color = UtpTextSecondary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UtpTextSecondary)
        }
    }
}

@Composable
fun StudentPracticeScreen(onComplete: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                Text("Aprendemos a contar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Señala cada manzana. Di un número cada vez.", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { Toast.makeText(context, "Reproduciendo audio - En proceso de conexión a BD", Toast.LENGTH_SHORT).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpOrangeSoft)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = UtpOrangeHighlight)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Escuchar", color = UtpOrangeHighlight, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🍎 🍎 🍎", fontSize = 40.sp)
                    Text("1    2    3", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpOrangeHighlight)
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(shape = RoundedCornerShape(12.dp), color = UtpOrangeSoft, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("3", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = UtpOrangeHighlight)
                            Text("¡Hay 3! La última cuenta dice cuántas hay.", fontSize = 12.sp, color = UtpTextPrimary)
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UtpOrangeHighlight)
            ) {
                Text("Vamos a practicar", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun StudentQuizScreen(onSubmit: () -> Unit, onBack: () -> Unit) {
    var selectedOption by remember { mutableStateOf("5") }
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                Text("Cuenta las manzanas", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¿Cuántas manzanas hay?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("🍎 🍎 🍎 🍎 🍎", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf("4", "5", "6").forEach { opt ->
                            OutlinedButton(
                                onClick = { selectedOption = opt },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selectedOption == opt) UtpOrangeSoft else Color.Transparent)
                            ) {
                                Text(opt, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (selectedOption == opt) UtpOrangeHighlight else UtpTextPrimary)
                            }
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UtpOrangeHighlight)
            ) {
                Text("Comprobar respuesta", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun StudentResultsScreen(onRetry: () -> Unit, onBack: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 40.sp)
                    Text("Prueba terminada", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("4/5 Correctas - 80%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = UtpOrangeHighlight)
                    Text("¡Excelente trabajo!", fontSize = 14.sp, color = UtpTextSecondary)
                }
            }
        }
        item {
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UtpOrangeHighlight)
            ) {
                Text("Repetir la prueba", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        item {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver al inicio", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            }
        }
    }
}

@Composable
fun StudentSearchScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Buscador de recursos y unidades UTP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(true) }
    var highContrast by remember { mutableStateOf(false) }
    var audioBackground by remember { mutableStateOf(true) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                Text("Ajustes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Notificaciones académicas", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Alertas de tareas y exámenes", fontSize = 13.sp)
                        Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = UtpCardBackground)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Accesibilidad", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Modo alto contraste", fontSize = 13.sp)
                        Switch(checked = highContrast, onCheckedChange = { highContrast = it })
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Audio en segundo plano", fontSize = 13.sp)
                        Switch(checked = audioBackground, onCheckedChange = { audioBackground = it })
                    }
                }
            }
        }
    }
}