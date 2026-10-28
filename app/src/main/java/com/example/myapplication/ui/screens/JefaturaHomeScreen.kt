package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
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
import com.example.myapplication.ui.components.DbStatusBadge
import com.example.myapplication.ui.model.AsignaturaUTP
import com.example.myapplication.ui.model.EstadoContenido
import com.example.myapplication.ui.model.UnidadPedagogica
import com.example.myapplication.ui.repository.UtpRepository
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JefaturaHomeScreen(
    onNavigateTo: (String) -> Unit
) {
    // 1. Suscripción al estado reactivo del Repository
    val unidades by UtpRepository.unidadesState.collectAsState()
    val historial by UtpRepository.historialState.collectAsState()

    // 2. Estados locales de la vista
    var asignaturaFiltro by remember { mutableStateOf(AsignaturaUTP.LENGUAJE) }
    var unidadARechazar by remember { mutableStateOf<UnidadPedagogica?>(null) }
    var textoObservacion by remember { mutableStateOf("") }

    // Unidades filtradas por la asignatura seleccionada
    val unidadesFiltradas = unidades.filter { it.asignatura == asignaturaFiltro }

    // Cálculo dinámico de métricas
    val pendientesCount = unidades.count { it.estado == EstadoContenido.PENDIENTE }
    val aprobadasCount = unidades.count { it.estado == EstadoContenido.APROBADO || it.estado == EstadoContenido.PUBLICADO }
    val historialCount = historial.size

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(UtpWarmBackground)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Tarjeta de Perfil
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
                                text = "PERFIL ADMINISTRADOR PEDAGÓGICO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = UtpTextSecondary
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = UtpOrangeSoft
                            ) {
                                Text(
                                    text = "1° Básico UTP",
                                    color = UtpOrangeHighlight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = UtpOrangeSoft
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = UtpOrangeHighlight,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Jefatura de UTP",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpTextPrimary
                                )
                                Text(
                                    text = "Supervisión Primer Ciclo",
                                    fontSize = 13.sp,
                                    color = UtpTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Métricas Dinámicas (Calculadas desde el Repository)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Pendientes", pendientesCount.toString(), Icons.Default.Schedule, Modifier.weight(1f))
                    StatCard("Aprobados", aprobadasCount.toString(), Icons.Default.CheckCircle, Modifier.weight(1f))
                    StatCard("Auditorías", historialCount.toString(), Icons.AutoMirrored.Filled.Assignment, Modifier.weight(1f))
                }
            }

            // Selector de las 4 Asignaturas
            item {
                Text(
                    text = "Asignaturas (1° Básico)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AsignaturaUTP.values().toList()) { asig ->
                        FilterChip(
                            selected = asignaturaFiltro == asig,
                            onClick = { asignaturaFiltro = asig },
                            label = { Text(asig.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UtpOrangeHighlight,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Lista de Unidades Pedagógicas e Interacción UTP
            item {
                Text(
                    text = "Unidades de ${asignaturaFiltro.displayName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary
                )
            }

            if (unidadesFiltradas.isEmpty()) {
                item {
                    Text(
                        text = "No hay unidades registradas para esta asignatura.",
                        fontSize = 13.sp,
                        color = UtpTextSecondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(unidadesFiltradas) { unidad ->
                    UnidadPedagogicaCard(
                        unidad = unidad,
                        onAprobar = { UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.APROBADO) },
                        onPublicar = { UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.PUBLICADO) },
                        onDeshabilitar = { UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.DESHABILITADO, "Deshabilitado para correcciones") },
                        onSolicitarRechazo = { unidadARechazar = unidad }
                    )
                }
            }

            // Historial de Modificaciones y Revisiones (Auditoría)
            item {
                Text(
                    text = "Historial de Modificaciones y Aprobaciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary
                )
            }

            if (historial.isEmpty()) {
                item {
                    Text(
                        text = "Aún no hay acciones registradas en el historial.",
                        fontSize = 13.sp,
                        color = UtpTextSecondary
                    )
                }
            } else {
                items(historial.take(5)) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                    color = UtpOrangeHighlight
                                )
                                Text(
                                    text = log.usuario,
                                    fontSize = 11.sp,
                                    color = UtpTextSecondary
                                )
                            }
                            Text(
                                text = log.detalle,
                                fontSize = 12.sp,
                                color = UtpTextPrimary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // Diálogo para Ingresar Observaciones al Rechazar
        unidadARechazar?.let { unidad ->
            AlertDialog(
                onDismissRequest = { unidadARechazar = null },
                title = { Text("Rechazar Contenido") },
                text = {
                    Column {
                        Text("Indique las observaciones pedagógicas para el docente:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = textoObservacion,
                            onValueChange = { textoObservacion = it },
                            placeholder = { Text("Ej: El objetivo no corresponde a 1° Básico...") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (textoObservacion.isNotBlank()) {
                                UtpRepository.cambiarEstadoUnidad(
                                    unidad.id,
                                    EstadoContenido.RECHAZADO,
                                    textoObservacion
                                )
                                textoObservacion = ""
                                unidadARechazar = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Confirmar Rechazo")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { unidadARechazar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Indicador inferior de Estado de BD
        DbStatusBadge(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            statusText = "En proceso de conexión a BD (Operando en memoria local)",
            isVisible = true
        )
    }
}

// Componente para las tarjetas de estadísticas superiores
@Composable
fun StatCard(
    title: String,
    count: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = UtpPurplePrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = UtpTextSecondary
            )
        }
    }
}

// Componente para representar una Unidad Pedagógica e interactuar con ella
@Composable
fun UnidadPedagogicaCard(
    unidad: UnidadPedagogica,
    onAprobar: () -> Unit,
    onPublicar: () -> Unit,
    onDeshabilitar: () -> Unit,
    onSolicitarRechazo: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unidad ${unidad.numero}: ${unidad.titulo}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = UtpTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                // Badge de Estado
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when(unidad.estado) {
                        EstadoContenido.APROBADO -> UtpBadgeGreenBg
                        EstadoContenido.PUBLICADO -> UtpBadgeBlueBg
                        EstadoContenido.RECHAZADO -> UtpBadgeRedBg
                        else -> UtpOrangeSoft
                    }
                ) {
                    Text(
                        text = unidad.estado.name,
                        color = when(unidad.estado) {
                            EstadoContenido.APROBADO -> UtpBadgeGreenText
                            EstadoContenido.PUBLICADO -> UtpBadgeBlueText
                            EstadoContenido.RECHAZADO -> UtpBadgeRedText
                            else -> UtpOrangeHighlight
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = unidad.descripcion, fontSize = 12.sp, color = UtpTextSecondary)

            if (!unidad.observacionesUTP.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Observación UTP: ${unidad.observacionesUTP}",
                    fontSize = 11.sp,
                    color = UtpBadgeRedText,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de Flujo UTP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onAprobar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UtpBadgeGreenText)
                ) {
                    Text("Aprobar", fontSize = 10.sp)
                }

                Button(
                    onClick = onPublicar,
                    modifier = Modifier.weight(1f),
                    enabled = unidad.estado == EstadoContenido.APROBADO,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Publicar", fontSize = 10.sp)
                }

                OutlinedButton(
                    onClick = onSolicitarRechazo,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Rechazar", fontSize = 10.sp, color = UtpBadgeRedText)
                }

                IconButton(onClick = onDeshabilitar, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Clear, contentDescription = "Deshabilitar", tint = Color.Gray)
                }
            }
        }
    }
}