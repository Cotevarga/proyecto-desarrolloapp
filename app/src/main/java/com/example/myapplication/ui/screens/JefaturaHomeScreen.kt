package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.theme.*

@Composable
fun JefaturaHomeScreen(
    onNavigateTo: (String) -> Unit
) {
    var isConnectingDb by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(UtpWarmBackground)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PERFIL ACTIVO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = UtpTextSecondary
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = UtpOrangeSoft
                            ) {
                                Text(
                                    text = "Jefatura UTP",
                                    color = UtpOrangeHighlight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = UtpOrangeSoft
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = UtpOrangeHighlight,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Maria Angelica",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = UtpBadgeGreenBg
                                    ) {
                                        Text(
                                            text = "Activo",
                                            color = UtpBadgeGreenText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Escuela Escritora Marcela Paz",
                                    fontSize = 13.sp,
                                    color = UtpTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Summary Stats (Cursos 2, Clases hoy 3, Tareas 12)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Cursos", "2", Icons.Default.School, Modifier.weight(1f))
                    StatCard("Clases hoy", "3", Icons.Default.CalendarToday, Modifier.weight(1f))
                    StatCard("Tareas", "12", Icons.AutoMirrored.Filled.Assignment, Modifier.weight(1f))
                }
            }

            // Section: Accesos Administrativos
            item {
                Text(
                    text = "Accesos Administrativos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminAccessCard(
                        title = "Gestión de Alumnos",
                        subtitle = "Asignación de cursos y horarios",
                        icon = Icons.Default.People,
                        onClick = { onNavigateTo("admin_subjects") }
                    )
                    AdminAccessCard(
                        title = "Gestión de Profesores",
                        subtitle = "Asignación de cursos y horarios",
                        icon = Icons.Default.PersonOutline,
                        onClick = { onNavigateTo("teacher_home") }
                    )
                    AdminAccessCard(
                        title = "Planificación Curricular",
                        subtitle = "Programas de estudio y objetivos",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        onClick = { onNavigateTo("unit_editor") }
                    )
                    AdminAccessCard(
                        title = "Calendario Académico",
                        subtitle = "Fechas de evaluación y exámenes",
                        icon = Icons.Default.Event,
                        onClick = { onNavigateTo("activity_detail") }
                    )
                }
            }

            // Section: Acciones Académicas
            item {
                Text(
                    text = "Acciones académicas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Highlighted Orange Card: Publicar Material
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateTo("library") },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpOrangeHighlight)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.HomeWork,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Publicar material",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Subir notas y recursos para los cursos activos.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
                        }
                    }

                    AdminAccessCard(
                        title = "Administrar Asignaturas",
                        subtitle = "Consultar clases, tutorías y fechas clave.",
                        icon = Icons.Default.ImportContacts,
                        onClick = { onNavigateTo("admin_subjects") }
                    )

                    AdminAccessCard(
                        title = "Revisar entregas",
                        subtitle = "Abrir lista de tareas pendientes y calificaciones.",
                        icon = Icons.Default.FavoriteBorder,
                        onClick = { onNavigateTo("course_progress") }
                    )
                }
            }

            // Section: Profesores a cargo
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profesores a cargo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                    TextButton(onClick = { /* See all */ }) {
                        Text("Ver todos", fontSize = 12.sp, color = UtpTextSecondary)
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TeacherInChargeItem("Magdalena", "1ro. Básico A • 42 estudiantes", "Historia")
                    TeacherInChargeItem("Alejandra", "1er. Básico A • 40 estudiantes", "Álgebra")
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // DB status badge pinned at bottom
        DbStatusBadge(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            statusText = "En proceso de conexión a BD",
            isVisible = isConnectingDb
        )
    }
}

@Composable
fun StatCard(title: String, count: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text(text = title, fontSize = 11.sp, color = UtpTextSecondary)
        }
    }
}

@Composable
fun AdminAccessCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = UtpOrangeSoft,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = UtpOrangeHighlight, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = UtpTextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = UtpTextSecondary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UtpTextSecondary)
        }
    }
}

@Composable
fun TeacherInChargeItem(name: String, details: String, subject: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = UtpOrangeSoft,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = name.take(1), fontWeight = FontWeight.Bold, color = UtpOrangeHighlight)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = UtpTextPrimary)
                Text(text = details, fontSize = 11.sp, color = UtpTextSecondary)
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = UtpOrangeSoft
            ) {
                Text(
                    text = subject,
                    color = UtpOrangeHighlight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}
