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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.theme.*

@Composable
fun TeacherHomeScreen(
    onNavigateTo: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                            Text("PERFIL ACTIVO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                            Surface(shape = RoundedCornerShape(12.dp), color = UtpPurpleSoft) {
                                Text(
                                    text = "Profesor",
                                    color = UtpPurplePrimary,
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
                                color = UtpPurpleSoft
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = UtpPurplePrimary)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Maria Angelica", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(shape = RoundedCornerShape(10.dp), color = UtpBadgeGreenBg) {
                                        Text("Activo", color = UtpBadgeGreenText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("Escuela Escritora Marcela Paz", fontSize = 13.sp, color = UtpTextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Periodo", fontSize = 10.sp, color = UtpTextSecondary)
                                Text("2024-2025", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Nivel", fontSize = 10.sp, color = UtpTextSecondary)
                                Text("Básica", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Ciclo", fontSize = 10.sp, color = UtpTextSecondary)
                                Text("1er Semestre", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Clases de hoy
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Clases de hoy", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Text("Agenda", fontSize = 12.sp, color = UtpTextSecondary)
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TodayClassCard("09:00 - 10:30", "Matemáticas Básica", "1A • 42 estudiantes", "Sala 204")
                    TodayClassCard("11:00 - 12:30", "Álgebra Lineal", "1B • 38 estudiantes", "Sala 307")
                }
            }

            // Summary Stats
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Cursos", "2", Icons.Default.School, Modifier.weight(1f))
                    StatCard("Clases hoy", "3", Icons.Default.CalendarToday, Modifier.weight(1f))
                    StatCard("Tareas", "12", Icons.AutoMirrored.Filled.Assignment, Modifier.weight(1f))
                }
            }

            // Cursos asignados
            item {
                Text("Cursos asignados", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onNavigateTo("course_progress") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = UtpPurpleSoft, modifier = Modifier.size(36.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("M", fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Matemáticas Básicas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Text("2do. Año • 42 estudiantes", fontSize = 11.sp, color = UtpTextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(10.dp), color = UtpPurpleSoft) {
                            Text("2 tareas", color = UtpPurplePrimary, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        DbStatusBadge(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
            statusText = "En proceso de conexión a BD",
            isVisible = true
        )
    }
}

@Composable
fun TodayClassCard(time: String, name: String, students: String, room: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = UtpTextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(time, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(students, fontSize = 12.sp, color = UtpTextSecondary)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = UtpBadgeGreenBg) {
                Text(room, color = UtpBadgeGreenText, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}
