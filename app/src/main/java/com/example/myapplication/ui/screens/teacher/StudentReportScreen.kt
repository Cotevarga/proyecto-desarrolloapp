package com.example.myapplication.ui.screens.teacher

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
fun StudentReportScreen(
    studentName: String = "Lucas Morales",
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    var informeValidado by remember { mutableStateOf(false) }
    var observacionPedagogica by remember {
        mutableStateOf("El estudiante muestra gran agilidad en conteo del 1 al 7. Se sugiere reforzar secuencias finales (8 al 10) con fichas concretas.")
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                showBack = true,
                onBack = onBack,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Informe Pedagógico Individual", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Text("Estudiante: $studentName • 1° Básico A", fontSize = 12.sp, color = UtpTextSecondary)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (informeValidado) UtpBadgeGreenBg else UtpBadgeRedBg
                        ) {
                            Text(
                                text = if (informeValidado) "✓ VALIDADO" else "BORRADOR",
                                color = if (informeValidado) UtpBadgeGreenText else UtpBadgeRedText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Rendimiento por OA
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Rendimiento por Objetivo (OA1)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("80%", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = UtpPurplePrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("4 de 5 respuestas correctas", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Text("Logro: Nivel Adecuado para 1° Básico", fontSize = 11.sp, color = UtpBadgeGreenText)
                                }
                            }
                        }
                    }
                }

                // Fortalezas Identificadas
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = UtpBadgeGreenText, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Fortalezas Identificadas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Reconoce y asocia correctamente colecciones hasta 7 objetos.", fontSize = 12.sp, color = UtpTextSecondary)
                            Text("• Completó los 3 desafíos interactivos en el primer intento.", fontSize = 12.sp, color = UtpTextSecondary)
                        }
                    }
                }

                // Contenidos Débiles a Reforzar
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = UtpBadgeRedText, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Contenidos Débiles a Reforzar", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Confusión menor en la pregunta 5 (secuencia directa 8 a 10).", fontSize = 12.sp, color = UtpTextSecondary)
                            Text("• Se recomienda ejercicios de refuerzo de conteo con material concreto.", fontSize = 12.sp, color = UtpTextSecondary)
                        }
                    }
                }

                // Observación Pedagógica Docente (MVP)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.EditNote, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Observación Pedagógica Docente (MVP)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = observacionPedagogica,
                                onValueChange = { observacionPedagogica = it },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                shape = RoundedCornerShape(10.dp),
                                placeholder = { Text("Escribe una observación pedagógica...", fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // Botones de Validación y Descarga
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                informeValidado = true
                                Toast.makeText(context, "Informe validado correctamente por el docente.", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (informeValidado) "Informe Validado" else "Validar Informe Pedagógico", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = {
                                if (informeValidado) {
                                    Toast.makeText(context, "Descargando informe oficial en PDF...", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Descargando borrador no validado...", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = UtpTextPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (informeValidado) "Descargar Informe PDF (Validado)" else "Descargar Borrador (No Validado)", fontSize = 13.sp, color = UtpTextPrimary)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}