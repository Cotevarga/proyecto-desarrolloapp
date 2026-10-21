package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Detalle de actividad", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = UtpPurpleSoft
                    ) {
                        Text(
                            text = "3 perfiles",
                            color = UtpPurplePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UtpWarmBackground)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Actividad académica",
                                fontSize = 11.sp,
                                color = UtpTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Revisión de caso clínico",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpTextPrimary
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = UtpPurpleSoft
                                ) {
                                    Text(
                                        text = "Mié 12 sep",
                                        color = UtpPurplePrimary,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Materia: Medicina Interna • Curso: 4° año",
                                fontSize = 13.sp,
                                color = UtpTextSecondary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Horario", fontSize = 11.sp, color = UtpTextSecondary)
                                    Text("08:30 - 10:00", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                }
                                Column {
                                    Text("Recordatorio", fontSize = 11.sp, color = UtpTextSecondary)
                                    Text("15 min antes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Detalles de la sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailRow("Fecha", "Mié 12 sep 2025", "Horario", "08:30 - 10:00")
                            HorizontalDivider()
                            DetailRow("Materia o curso", "Medicina Interna • 4° año", "Recordatorio", "15 min antes")
                            HorizontalDivider()
                            DetailRow("Modalidad o ubicación", "Presencial - Sala 4B", "Perfil apoyo", "Alumno, Profesor, Jefatura")
                        }
                    }
                }

                item {
                    Text(
                        text = "Descripción",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Text(
                            text = "Revisión guiada de un caso clínico con presentación, discusión grupal y retroalimentación docente. Los estudiantes deben llegar con la lectura previa resumida y listos para participar en la ronda de preguntas.",
                            fontSize = 13.sp,
                            color = UtpTextPrimary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = "Perfiles apoyo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = UtpPurplePrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Alumno", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

        DbStatusBadge(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            statusText = "En proceso de conexión a BD",
            isVisible = true
        )
    }
}

@Composable
fun DetailRow(label1: String, value1: String, label2: String, value2: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label1, fontSize = 11.sp, color = UtpTextSecondary)
            Text(value1, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label2, fontSize = 11.sp, color = UtpTextSecondary)
            Text(value2, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
        }
    }
}
