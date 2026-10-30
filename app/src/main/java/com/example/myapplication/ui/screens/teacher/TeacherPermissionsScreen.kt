package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun TeacherPermissionsScreen(
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    var filtroEstado by remember { mutableStateOf("Todos") }
    val listaSolicitudes = remember { repository.getSolicitudesPermiso() }

    val solicitudesFiltradas = remember(filtroEstado, listaSolicitudes) {
        when (filtroEstado) {
            "En Proceso" -> listaSolicitudes.filter { it.estado == "PENDIENTE" }
            "Aceptados" -> listaSolicitudes.filter { it.estado == "ACEPTADO" }
            "Rechazados" -> listaSolicitudes.filter { it.estado == "RECHAZADO" }
            else -> listaSolicitudes
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                showBack = false,
                onAccountSettings = onAccountSettings,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    Column {
                        Text(
                            text = "Historial de Permisos y Solicitudes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Text(
                            text = "Registro de tramitaciones y respuestas de Jefatura UTP",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                // Chips de filtrado por estado
                item {
                    val estadosFiltro = listOf("Todos", "En Proceso", "Aceptados", "Rechazados")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(estadosFiltro) { est ->
                            FilterChip(
                                selected = filtroEstado == est,
                                onClick = { filtroEstado = est },
                                label = { Text(est, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = UtpPurplePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Lista de Solicitudes (Historial)
                if (solicitudesFiltradas.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AssignmentTurnedIn,
                                    contentDescription = null,
                                    tint = UtpTextSecondary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No hay solicitudes registradas en esta categoría.",
                                    fontSize = 12.sp,
                                    color = UtpTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(solicitudesFiltradas) { sol ->
                        val (badgeBg, badgeText, textoEstado) = when (sol.estado) {
                            "ACEPTADO" -> Triple(UtpBadgeGreenBg, UtpBadgeGreenText, "✓ ACEPTADO")
                            "RECHAZADO" -> Triple(UtpBadgeRedBg, UtpBadgeRedText, "✗ RECHAZADO")
                            else -> Triple(UtpBadgeBlueBg, UtpBadgeBlueText, "⏳ EN PROCESO")
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = UtpPurpleSoft
                                    ) {
                                        Text(
                                            text = sol.tipoPermiso,
                                            fontSize = 10.sp,
                                            color = UtpPurplePrimary,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = badgeBg
                                    ) {
                                        Text(
                                            text = textoEstado,
                                            color = badgeText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(sol.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                Text("Enviado: ${sol.fechaSolicitud}", fontSize = 11.sp, color = UtpTextSecondary)

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Motivo: ${sol.motivoDocente}", fontSize = 12.sp, color = UtpTextPrimary)

                                sol.respuestaUtp?.let { resp ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        color = UtpWarmBackground
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text("Respuesta UTP:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                                            Text(resp, fontSize = 11.sp, color = UtpTextPrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}