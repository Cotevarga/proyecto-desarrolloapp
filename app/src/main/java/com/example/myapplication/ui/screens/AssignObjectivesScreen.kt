package com.example.myapplication.ui.screens

import com.example.myapplication.ui.model.ObjetivoAprendizaje
import com.example.myapplication.ui.repository.UtpRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
fun AssignObjectivesScreen(
    onBack: () -> Unit,
    onAssignedSuccess: () -> Unit
) {
    var startDate by remember { mutableStateOf("22/10/2025 - 08:00") }
    var endDate by remember { mutableStateOf("29/10/2025 - 18:00") }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text("Asignar objetivos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Curso 1° A • Configuración docente", fontSize = 12.sp, color = UtpTextSecondary)
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
                item {
                    Text("Objetivo seleccionado", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(shape = RoundedCornerShape(8.dp), color = UtpOrangeSoft) {
                                Text("Matemática", color = UtpOrangeHighlight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Contar y representar cantidades del 1 al 10.", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Text("Autorizado por UTP • v1.2", fontSize = 11.sp, color = UtpTextSecondary)
                        }
                    }
                }

                item {
                    Text("Actividades asignadas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ChecklistItem("Aprender y ejemplo guiado")
                            ChecklistItem("Practicar: conteo progresivo")
                            ChecklistItem("Desafíos: 1, 2 y 3")
                            ChecklistItem("Prueba final: 5 preguntas del OA")
                        }
                    }
                }

                item {
                    Text("Disponibilidad de la prueba", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = startDate,
                                onValueChange = { startDate = it },
                                label = { Text("Desde - Fecha y hora") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = endDate,
                                onValueChange = { endDate = it },
                                label = { Text("Hasta - Fecha y hora") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Surface(shape = RoundedCornerShape(8.dp), color = UtpOrangeSoft, modifier = Modifier.fillMaxWidth()) {
                                Text("Máximo de intentos: 2 intentos", color = UtpOrangeHighlight, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = onAssignedSuccess,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Asignar y habilitar con estas reglas", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
fun ChecklistItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Check, contentDescription = null, tint = UtpBadgeGreenText, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, color = UtpTextPrimary)
    }
}
