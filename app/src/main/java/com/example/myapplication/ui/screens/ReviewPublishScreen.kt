package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
fun ReviewPublishScreen(
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text("Revisión y publicación", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Matemática • Contamos del 1 al 10 - v1.3", fontSize = 12.sp, color = UtpTextSecondary)
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
                    Surface(shape = RoundedCornerShape(8.dp), color = UtpOrangeSoft) {
                        Text("En revisión - Cargo UTP 02", color = UtpOrangeHighlight, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Checklist de autorización", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)

                            ChecklistRow("Objetivo y referencia curricular cotejados en MINEDUC.")
                            ChecklistRow("Las 5 preguntas están alineadas al OA Conteo 1-10.")
                            ChecklistRow("Textos e imágenes propios; autorizaciones de uso registradas.")
                            ChecklistRow("Audios preparados, claros y reproducibles.")
                            ChecklistRow("Alternativas y claves verificadas: 3, 5, 7, 4 y 9.")
                        }
                    }
                }

                item {
                    Button(
                        onClick = { /* Aprobar */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Aprobar • pasar a autorizado", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                item {
                    OutlinedButton(
                        onClick = { /* Devolver */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Devolver con observaciones", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpBadgeRedText)
                    }
                }

                item {
                    Text("Historial de cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            HistoryItem("21 oct • 09:00 - v1.2", "Versión publicada", "Disponible para selección docente. Autorización registrada por UTP 02.", UtpBadgeGreenBg, UtpBadgeGreenText)
                            HistoryItem("21 oct • 08:45 - v1.2", "Versión aprobada", "Se verificaron alineación al OA y recursos autorizados.", UtpBadgeGreenBg, UtpBadgeGreenText)
                            HistoryItem("20 oct • 14:00 - v1.2", "Corrección y reenvío", "Texto alternativo actualizado.", UtpOrangeSoft, UtpOrangeHighlight)
                            HistoryItem("19 oct • 08:30 - v1.0", "Unidad creada", "Se creó Contamos del 1 al 10 para 1.° Básico.", UtpPurpleSoft, UtpPurplePrimary)
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
fun ChecklistRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = UtpBadgeGreenText, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp, color = UtpTextPrimary)
    }
}

@Composable
fun HistoryItem(date: String, title: String, detail: String, badgeBg: Color, badgeText: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(date, fontSize = 10.sp, color = UtpTextSecondary)
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            Text(detail, fontSize = 11.sp, color = UtpTextSecondary)
        }
        Surface(shape = RoundedCornerShape(8.dp), color = badgeBg) {
            Text(title.take(10), color = badgeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
    }
}
