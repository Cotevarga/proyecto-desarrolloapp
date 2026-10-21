package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
fun UnitEditorScreen(
    onBack: () -> Unit
) {
    var unitTitle by remember { mutableStateOf("Contamos del 1 al 10") }
    var unitLevel by remember { mutableStateOf("1.° Básico") }
    var objectiveText by remember { mutableStateOf("Contar y representar cantidades del 1 al 10 mediante objetos, imágenes y números.") }
    var selectedContentType by remember { mutableStateOf("Desafío") }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text("Unidad y objetivos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Matemática • Gestión curricular", fontSize = 12.sp, color = UtpTextSecondary)
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
                    Text("Datos de la unidad", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = unitTitle,
                                onValueChange = { unitTitle = it },
                                label = { Text("Título de la unidad") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = unitLevel,
                                onValueChange = { unitLevel = it },
                                label = { Text("Nivel") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                item {
                    Text("Objetivo de aprendizaje", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = objectiveText,
                                onValueChange = { objectiveText = it },
                                label = { Text("Descripción didáctica") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                minLines = 2
                            )
                        }
                    }
                }

                item {
                    Text("Tipo de contenido", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilterChip(
                            selected = selectedContentType == "Actividad",
                            onClick = { selectedContentType = "Actividad" },
                            label = { Text("Actividad") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = selectedContentType == "Desafío",
                            onClick = { selectedContentType = "Desafío" },
                            label = { Text("Desafío") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Button(
                        onClick = { /* Guardar */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Guardar cambios de la unidad", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                item {
                    OutlinedButton(
                        onClick = { /* Nueva unidad */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Crear nueva unidad", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
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
