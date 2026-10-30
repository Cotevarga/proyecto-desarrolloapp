package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
    onAccountSettings: () -> Unit = {}, // <-- Parámetro agregado
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    val solicitudes = remember { repository.getSolicitudesPermiso() }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                showBack = false,
                onAccountSettings = onAccountSettings, // <-- Conectado
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
                            text = "Estado de solicitudes enviadas a Jefatura UTP",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                items(solicitudes) { sol ->
                    val (badgeBg, badgeText) = when (sol.estado) {
                        "ACEPTADO" -> Pair(UtpBadgeGreenBg, UtpBadgeGreenText)
                        "PENDIENTE" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
                        else -> Pair(Color(0xFFFEE2E2), Color(0xFFDC2626))
                    }

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
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = UtpPurpleSoft
                                ) {
                                    Text(
                                        text = sol.tipoPermiso,
                                        color = UtpPurplePrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = badgeBg
                                ) {
                                    Text(
                                        text = sol.estado,
                                        color = badgeText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(sol.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Fecha: ${sol.fechaSolicitud}", fontSize = 11.sp, color = UtpTextSecondary)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Motivo:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Text(sol.motivoDocente, fontSize = 11.sp, color = UtpTextSecondary)

                            sol.respuestaUtp?.let { resp ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF1F5F9),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Respuesta UTP:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                                        Text(resp, fontSize = 11.sp, color = UtpTextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}