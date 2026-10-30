package com.example.myapplication.ui.screens.student

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun StudentUnitDetailScreen(
    onBack: () -> Unit = {},
    onStartQuiz: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    var desafiosCompletados by remember { mutableStateOf(true) }
    var intentosRealizados by remember { mutableStateOf(1) }
    val maxIntentos = 2

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "ALUMNO",
                userName = "Camila Morales (1° A)",
                showBack = true,
                onBack = onBack,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                // OBJETIVO ASIGNADO POR EL DOCENTE CON AUDIO
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(shape = RoundedCornerShape(6.dp), color = UtpRed.copy(alpha = 0.12f)) {
                                    Text("OA1 • Asignado por Profesora", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UtpRed, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }

                                IconButton(
                                    onClick = {
                                        Toast.makeText(context, "🔊 Reproduciendo instrucciones en voz alta...", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar Instrucciones", tint = UtpRed)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Unidad 1: Contamos y representamos del 1 al 10", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Text("Cuenta objetos cotidianos del 1 al 10 y asocia las cantidades con sus números.", fontSize = 12.sp, color = UtpTextSecondary)
                        }
                    }
                }

                // EJEMPLO GUIADO
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Ejemplo Guiado", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                                Text("🍎🍎🍎 = 3 Manzanas. ¡Cuenta con tus dedos!", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = UtpTextPrimary)
                            }
                        }
                    }
                }

                // PASOS DE APRENDIZAJE
                item {
                    Text("Pasos de Aprendizaje", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StudentStepItem(stepNumber = "1", title = "Actividad 1: Trazado del 1 al 5", subtitle = "Ficha interactiva de números", isCompleted = true)
                        StudentStepItem(stepNumber = "2", title = "Actividad 2: Conteo de elementos", subtitle = "Empareja cantidades con figuras", isCompleted = true)
                        StudentStepItem(stepNumber = "3", title = "Desafío Rápido: La recta numérica", subtitle = "Encuentra el número que falta", isCompleted = desafiosCompletados)
                    }
                }

                // CARD RENDIR PRUEBA
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Prueba Final de la Unidad", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Text("Intentos: $intentosRealizados de $maxIntentos permitidos", fontSize = 11.sp, color = UtpTextSecondary)
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (desafiosCompletados) UtpBadgeGreenBg else Color(0xFFFEE2E2)
                                ) {
                                    Text(
                                        text = if (desafiosCompletados) "Listo para Rendir" else "Bloqueado",
                                        color = if (desafiosCompletados) UtpBadgeGreenText else Color(0xFFDC2626),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (!desafiosCompletados) {
                                        Toast.makeText(context, "Debes completar todos los desafíos antes de rendir la prueba", Toast.LENGTH_SHORT).show()
                                    } else if (intentosRealizados >= maxIntentos) {
                                        Toast.makeText(context, "Has alcanzado el límite máximo de $maxIntentos intentos definidos por tu docente.", Toast.LENGTH_LONG).show()
                                    } else {
                                        onStartQuiz()
                                    }
                                },
                                enabled = desafiosCompletados && intentosRealizados < maxIntentos,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = UtpRed),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Rendir Prueba Final", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun StudentStepItem(
    stepNumber: String,
    title: String,
    subtitle: String,
    isCompleted: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = UtpCardBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isCompleted) Color(0xFFD1FAE5) else UtpRed.copy(alpha = 0.12f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isCompleted) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp))
                    } else {
                        Text(stepNumber, fontWeight = FontWeight.Bold, color = UtpRed, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                Text(subtitle, fontSize = 11.sp, color = UtpTextSecondary)
            }
        }
    }
}