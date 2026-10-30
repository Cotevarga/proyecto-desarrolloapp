package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun TeacherHomeScreen(
    onNavigateTo: (String) -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    val docente = remember { repository.getDocenteById("doc_1") }
    val solicitudes = remember { repository.getSolicitudesPermiso() }
    val pendientesCount = solicitudes.count { it.estado == "PENDIENTE" }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                userName = docente.nombre,
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
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpPurplePrimary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "¡Hola, ${docente.nombre}!",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${docente.materiaPrincipal} • 1° Básico (1° A y 1° B)",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Icon(
                                        Icons.Default.School,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Estado General",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TeacherKpiCard(
                            title = "Unidades Pedagógicas",
                            count = "3 Activas",
                            subtitle = "Asignadas en Clases",
                            icon = Icons.Default.Class,
                            color = UtpPurplePrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("teacher_classes") }
                        )
                        TeacherKpiCard(
                            title = "Permisos UTP",
                            count = "$pendientesCount Pendientes",
                            subtitle = "En revisión por Jefatura",
                            icon = Icons.Default.AssignmentTurnedIn,
                            color = UtpOrangeHighlight,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("teacher_permissions") }
                        )
                    }
                }

                item {
                    Text(
                        text = "Acciones Rápidas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TeacherQuickCard(
                            title = "Nueva Solicitud",
                            icon = Icons.Default.AddCircleOutline,
                            color = UtpPurpleSoft,
                            iconColor = UtpPurplePrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("teacher_request") }
                        )
                        TeacherQuickCard(
                            title = "Avance Alumnos",
                            icon = Icons.Default.People,
                            color = UtpBadgeBlueBg,
                            iconColor = UtpBadgeBlueText,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("teacher_students") }
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFD1FAE5),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Sincronizado con UTP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                )
                                Text(
                                    text = "Unidades 1, 2 y 3 habilitadas para asignación",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpTextPrimary
                                )
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
private fun TeacherKpiCard(
    title: String,
    count: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(count, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = UtpTextPrimary)
            Text(subtitle, fontSize = 10.sp, color = UtpTextSecondary)
        }
    }
}

@Composable
private fun TeacherQuickCard(
    title: String,
    icon: ImageVector,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
        }
    }
}