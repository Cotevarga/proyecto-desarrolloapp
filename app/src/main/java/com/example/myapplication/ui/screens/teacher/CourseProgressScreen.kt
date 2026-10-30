package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.EstudianteAvance
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun CourseProgressScreen(
    onBack: () -> Unit = {},
    onSelectStudentReport: (EstudianteAvance) -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    var selectedStudentForModal by remember { mutableStateOf<EstudianteAvance?>(null) }

    // Obtención de datos centralizada desde TeacherRepository
    val estudiantes = remember { repository.getEstudiantesPorDocente("doc_1") }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                userName = "Prof. María Angélica",
                showBack = true,
                onBack = onBack,
                onAccountSettings = onAccountSettings,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    Column {
                        Text(
                            text = "Progreso de Alumnos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Text(
                            text = "1° Año Básico A • Asignatura Matemática",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                items(estudiantes) { est ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = est.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = UtpTextPrimary
                                    )
                                    Text(
                                        text = "Curso: ${est.curso} • ${est.oa}",
                                        fontSize = 11.sp,
                                        color = UtpTextSecondary
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (est.avance == "100%") UtpBadgeGreenBg else UtpPurpleSoft
                                ) {
                                    Text(
                                        text = est.avance,
                                        color = if (est.avance == "100%") UtpBadgeGreenText else UtpPurplePrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { selectedStudentForModal = est },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                            ) {
                                Text("Ver Avance", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        if (selectedStudentForModal != null) {
            val est = selectedStudentForModal!!
            AlertDialog(
                onDismissRequest = { selectedStudentForModal = null },
                title = {
                    Text(
                        text = "Respuestas: ${est.nombre} (${est.curso})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rendimiento y Avance en Prueba Final (OA1):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextSecondary
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = UtpPurpleSoft,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Actividad 1: Trazado 1-5", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Text("100%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Actividad 2: Conteo de elementos", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Text("100%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Prueba OA1", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Text(est.pruebaPuntaje, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF2F2),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "En proceso de integración de base de datos",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedStudentForModal = null }) {
                        Text("Cerrar", fontWeight = FontWeight.Bold, color = UtpPurplePrimary, fontSize = 13.sp)
                    }
                },
                dismissButton = null,
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}