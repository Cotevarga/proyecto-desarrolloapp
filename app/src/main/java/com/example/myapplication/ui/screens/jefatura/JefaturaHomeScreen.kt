package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.EstadoContenido
import com.example.myapplication.data.model.UnidadPedagogica
import com.example.myapplication.data.repository.UtpRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

val UtpCelestePrimary = Color(0xFF0284C7)

@Composable
fun JefaturaHomeScreen(
    onNavigateTo: (String) -> Unit,
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val unidades by UtpRepository.unidadesState.collectAsState()
    var cursoSeleccionado by remember { mutableStateOf("1° A") }

    val colorCursoActivo = if (cursoSeleccionado == "1° A") UtpCelestePrimary else UtpPurplePrimary

    val pendientesTotales = unidades.filter { it.estado == EstadoContenido.PENDIENTE }
    val pendientesDelCurso = pendientesTotales.filter { unidad ->
        if (cursoSeleccionado == "1° A") {
            !unidad.titulo.contains("1° B", true) && !unidad.descripcion.contains("1° B", true)
        } else {
            unidad.titulo.contains("1° B", true) || unidad.descripcion.contains("1° B", true)
        }
    }

    val publicadosTotales = unidades.count { it.estado == EstadoContenido.PUBLICADO }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "JEFATURA",
                showBack = false,
                onAccountSettings = onAccountSettings,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                // 1. Selector de Curso
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            listOf("1° A", "1° B").forEach { curso ->
                                val isSelected = cursoSeleccionado == curso
                                val containerColor = if (isSelected) {
                                    if (curso == "1° A") UtpCelestePrimary else UtpPurplePrimary
                                } else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(containerColor)
                                        .clickable { cursoSeleccionado = curso },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "1° Básico $curso",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else UtpTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Tarjetas KPI
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryKpiCard(
                            title = "Pendientes",
                            count = pendientesDelCurso.size.toString(),
                            subtitle = "Por autorizar ($cursoSeleccionado)",
                            icon = Icons.Default.PendingActions,
                            color = UtpBadgeRedText,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("admin_subjects") }
                        )
                        SummaryKpiCard(
                            title = "Publicaciones",
                            count = publicadosTotales.toString(),
                            subtitle = "Activas en el muro",
                            icon = Icons.Default.CheckCircle,
                            color = UtpBadgeGreenText,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("review_publish") }
                        )
                    }
                }

                // 3. Accesos Rápidos
                item {
                    Text(
                        text = "Accesos Rápidos",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionButton(
                            title = "Nueva Publicación",
                            icon = Icons.Default.AddCircleOutline,
                            color = UtpCelestePrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("unit_editor") }
                        )
                        QuickActionButton(
                            title = "Gestión por Materias",
                            icon = Icons.Default.Dashboard,
                            color = UtpPurplePrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateTo("admin_subjects") }
                        )
                    }
                }

                // 4. Solicitudes Recientes
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Solicitudes Recientes ($cursoSeleccionado)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        if (pendientesDelCurso.isNotEmpty()) {
                            TextButton(onClick = { onNavigateTo("admin_subjects") }) {
                                Text("Ver todas >", fontSize = 12.sp, color = colorCursoActivo, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (pendientesDelCurso.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = UtpBadgeGreenText,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Al día con $cursoSeleccionado", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                                    Text("No tienes solicitudes pendientes por autorizar.", fontSize = 12.sp, color = UtpTextSecondary)
                                }
                            }
                        }
                    }
                } else {
                    items(pendientesDelCurso.take(2)) { unidad ->
                        CompactPendingCard(
                            unidad = unidad,
                            onAprobar = { UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.APROBADO) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun SummaryKpiCard(
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(count, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = UtpTextPrimary)
            Text(subtitle, fontSize = 10.sp, color = UtpTextSecondary)
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompactPendingCard(
    unidad: UnidadPedagogica,
    onAprobar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${unidad.asignatura.displayName} • Unidad ${unidad.numero}",
                    fontSize = 10.sp,
                    color = UtpTextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = unidad.titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onAprobar,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UtpBadgeGreenText),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Aprobar", fontSize = 11.sp, color = Color.White)
            }
        }
    }
}