package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.EstadoContenido
import com.example.myapplication.data.repository.UtpRepository
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class FiltroHistorialUTP(val label: String) {
    TODOS("Todos los registros"),
    APROBADOS("Unidades Aprobadas"),
    PUBLICADOS("Unidades Publicadas")
}

@Composable
fun HistorialCambiosScreen(
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val historial by UtpRepository.historialState.collectAsState()
    val unidades by UtpRepository.unidadesState.collectAsState()

    var filtroSeleccionado by remember { mutableStateOf(FiltroHistorialUTP.TODOS) }

    val unidadesFiltradas = remember(unidades, filtroSeleccionado) {
        when (filtroSeleccionado) {
            FiltroHistorialUTP.TODOS -> unidades.filter {
                it.estado == EstadoContenido.APROBADO || it.estado == EstadoContenido.PUBLICADO
            }
            FiltroHistorialUTP.APROBADOS -> unidades.filter { it.estado == EstadoContenido.APROBADO }
            FiltroHistorialUTP.PUBLICADOS -> unidades.filter { it.estado == EstadoContenido.PUBLICADO }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                showBack = false,
                onAccountSettings = onAccountSettings,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "HISTORIAL Y AUDITORÍA UTP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpTextSecondary
                                )
                                Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF0284C7))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Registro de Autorizaciones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = UtpTextPrimary
                            )
                            Text(
                                text = "Consulta el listado de contenidos aprobados y la bitácora de cambios.",
                                fontSize = 12.sp,
                                color = UtpTextSecondary
                            )
                        }
                    }
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(FiltroHistorialUTP.values().toList()) { filtro ->
                            FilterChip(
                                selected = filtroSeleccionado == filtro,
                                onClick = { filtroSeleccionado = filtro },
                                label = { Text(filtro.label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0284C7),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Unidades Registradas (${unidadesFiltradas.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                if (unidadesFiltradas.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Text(
                                text = "No hay unidades registradas para el filtro seleccionado.",
                                fontSize = 12.sp,
                                color = UtpTextSecondary,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                } else {
                    items(unidadesFiltradas) { unidad ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = UtpBadgeGreenText,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${unidad.asignatura.displayName} • Unidad ${unidad.numero}",
                                        fontSize = 11.sp,
                                        color = UtpTextSecondary
                                    )
                                    Text(
                                        text = unidad.titulo,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpTextPrimary
                                    )
                                    Text(
                                        text = unidad.descripcion,
                                        fontSize = 11.sp,
                                        color = UtpTextSecondary,
                                        maxLines = 1
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (unidad.estado == EstadoContenido.PUBLICADO) UtpBadgeBlueBg else UtpBadgeGreenBg
                                ) {
                                    Text(
                                        text = unidad.estado.name,
                                        color = if (unidad.estado == EstadoContenido.PUBLICADO) UtpBadgeBlueText else UtpBadgeGreenText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (filtroSeleccionado == FiltroHistorialUTP.TODOS) {
                    item {
                        Text(
                            text = "Bitácora de Modificaciones (Auditoría)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                    }

                    if (historial.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                            ) {
                                Text(
                                    text = "Aún no hay acciones registradas en la bitácora de auditoría.",
                                    fontSize = 12.sp,
                                    color = UtpTextSecondary,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    } else {
                        items(historial) { log ->
                            val fechaFormateada = remember(log.timestamp) {
                                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(log.timestamp))
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = log.accion,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0284C7)
                                        )
                                        Text(
                                            text = "$fechaFormateada • ${log.usuario}",
                                            fontSize = 10.sp,
                                            color = UtpTextSecondary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = log.detalle,
                                        fontSize = 12.sp,
                                        color = UtpTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(60.dp)) }
            }
        }

        DbStatusBadge(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            statusText = "Conectado a memoria local (UtpRepository)",
            isVisible = true
        )
    }
}