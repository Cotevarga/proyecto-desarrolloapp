package com.example.myapplication.ui.screens

import com.example.myapplication.ui.model.EstadoContenido
import com.example.myapplication.ui.repository.UtpRepository
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun CourseProgressScreen(
    onBack: () -> Unit
) {
    var selectedStudent by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text("Progreso del curso", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("1° A • 4 estudiantes ficticios", fontSize = 12.sp, color = UtpTextSecondary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                // Progress Header
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("OA: Contar y representar cantidades del 1 al 10", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("2 de 4 completaron los desafíos • 70%", fontSize = 12.sp, color = UtpTextSecondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { 0.7f },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = UtpPurplePrimary,
                                trackColor = UtpPurpleSoft
                            )
                        }
                    }
                }

                item {
                    Text("Avance por estudiante", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StudentProgressItem("Estudiante 01", "Desafíos 3/3 • 4/5 correctas", "Finalizado", UtpBadgeGreenBg, UtpBadgeGreenText) {
                            selectedStudent = if (selectedStudent == "Estudiante 01") null else "Estudiante 01"
                        }
                        StudentProgressItem("Estudiante 02", "Pendiente - Completar desafío 3", "Bloqueado", UtpBadgeRedBg, UtpBadgeRedText) {
                            selectedStudent = if (selectedStudent == "Estudiante 02") null else "Estudiante 02"
                        }
                        StudentProgressItem("Estudiante 03", "Pregunta 2/5 - Intento 1/2", "En curso", UtpBadgeBlueBg, UtpBadgeBlueText) {
                            selectedStudent = if (selectedStudent == "Estudiante 03") null else "Estudiante 03"
                        }
                        StudentProgressItem("Estudiante 04", "Desafíos 3/3 • 5/5 correctas", "Finalizado", UtpBadgeGreenBg, UtpBadgeGreenText) {
                            selectedStudent = if (selectedStudent == "Estudiante 04") null else "Estudiante 04"
                        }
                    }
                }

                // Selected Student Report (Informe Pedagógico)
                item {
                    AnimatedVisibility(visible = selectedStudent != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Informe Pedagógico - ${selectedStudent ?: ""}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)

                                Surface(shape = RoundedCornerShape(12.dp), color = UtpPurpleSoft, modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text("80%", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                                        Column {
                                            Text("4 de 5 correctas", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            Text("1 incorrecta", fontSize = 12.sp, color = UtpBadgeRedText)
                                        }
                                    }
                                }

                                Text("Fortalezas observadas:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Resolvió correctamente el conteo de 3, 5, 7 y 4 objetos. Relacionó correctamente las colecciones con su número.", fontSize = 12.sp, color = UtpTextSecondary)

                                Text("Para reforzar:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("En la pregunta 5 eligió 8 para 9 bloques. Proponer un nuevo conteo para afianzar.", fontSize = 12.sp, color = UtpTextSecondary)

                                Button(
                                    onClick = { /* Validar */ },
                                    modifier = Modifier.fillMaxWidth().height(46.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Validar informe", color = Color.White, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { /* Descargar */ },
                                    modifier = Modifier.fillMaxWidth().height(46.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Descargar borrador - no validado", color = UtpTextPrimary)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
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
fun StudentProgressItem(
    name: String,
    detail: String,
    status: String,
    statusBg: Color,
    statusText: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(detail, fontSize = 11.sp, color = UtpTextSecondary)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = statusBg) {
                Text(status, color = statusText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UtpTextSecondary)
        }
    }
}
