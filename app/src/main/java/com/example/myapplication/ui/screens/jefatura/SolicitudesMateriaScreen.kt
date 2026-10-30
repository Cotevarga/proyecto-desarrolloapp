package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.AsignaturaUTP
import com.example.myapplication.data.model.EstadoContenido
import com.example.myapplication.data.model.UnidadPedagogica
import com.example.myapplication.data.repository.UtpRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

enum class TipoRecursoFiltro(val label: String) {
    TODOS("Todos"),
    UNIDADES("Unidades"),
    ACTIVIDADES("Actividades"),
    DESAFIOS("Desafíos"),
    IMAGENES("Imágenes"),
    AUDIOS("Audios"),
    PRUEBAS("Pruebas")
}

@Composable
fun SolicitudesMateriaScreen(
    subjectName: String,
    onBack: () -> Unit,
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val unidades by UtpRepository.unidadesState.collectAsState()
    var cursoSeleccionado by remember { mutableStateOf("1° A") }
    var filtroTipo by remember { mutableStateOf(TipoRecursoFiltro.TODOS) }

    var unidadARechazar by remember { mutableStateOf<UnidadPedagogica?>(null) }
    var textoObservacion by remember { mutableStateOf("") }

    val asignaturaEnum = remember(subjectName) {
        AsignaturaUTP.values().find {
            it.displayName.equals(subjectName, ignoreCase = true) || it.name.equals(subjectName, ignoreCase = true)
        } ?: AsignaturaUTP.LENGUAJE
    }

    val colorCursoActivo = if (cursoSeleccionado == "1° A") UtpCelestePrimary else UtpPurplePrimary

    val pendientesMateria = unidades.filter { unidad ->
        val esPendiente = unidad.estado == EstadoContenido.PENDIENTE
        val esMismaMateria = unidad.asignatura == asignaturaEnum

        val perteneceAlCurso = if (cursoSeleccionado == "1° A") {
            !unidad.titulo.contains("1° B", ignoreCase = true) && !unidad.descripcion.contains("1° B", ignoreCase = true)
        } else {
            unidad.titulo.contains("1° B", ignoreCase = true) || unidad.descripcion.contains("1° B", ignoreCase = true)
        }

        val esActividad = unidad.titulo.contains("Actividad", true) || unidad.descripcion.contains("Actividad", true) || unidad.titulo.contains("Guía", true)
        val esDesafio = unidad.titulo.contains("Desafío", true) || unidad.descripcion.contains("Desafío", true)
        val esImagen = unidad.titulo.contains("Imagen", true) || unidad.descripcion.contains("Imagen", true)
        val esAudio = unidad.titulo.contains("Audio", true) || unidad.descripcion.contains("Audio", true)
        val esPrueba = unidad.titulo.contains("Prueba", true) || unidad.descripcion.contains("Prueba", true)

        val coincideTipo = when (filtroTipo) {
            TipoRecursoFiltro.TODOS -> true
            TipoRecursoFiltro.UNIDADES -> unidad.numero > 0 || unidad.titulo.contains("Unidad", true) || (!esActividad && !esDesafio && !esImagen && !esAudio && !esPrueba)
            TipoRecursoFiltro.ACTIVIDADES -> esActividad
            TipoRecursoFiltro.DESAFIOS -> esDesafio
            TipoRecursoFiltro.IMAGENES -> esImagen
            TipoRecursoFiltro.AUDIOS -> esAudio
            TipoRecursoFiltro.PRUEBAS -> esPrueba
        }

        esPendiente && esMismaMateria && perteneceAlCurso && coincideTipo
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "JEFATURA",
                userName = "Jefe UTP",
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
                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    Text(
                        text = "Seleccionar Paralelo para ${asignaturaEnum.displayName}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { cursoSeleccionado = "1° A" },
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (cursoSeleccionado == "1° A") UtpCelestePrimary else Color.LightGray.copy(alpha = 0.3f),
                                contentColor = if (cursoSeleccionado == "1° A") Color.White else UtpTextPrimary
                            )
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("1° Básico A", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { cursoSeleccionado = "1° B" },
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (cursoSeleccionado == "1° B") UtpPurplePrimary else Color.LightGray.copy(alpha = 0.3f),
                                contentColor = if (cursoSeleccionado == "1° B") Color.White else UtpTextPrimary
                            )
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("1° Básico B", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                item {
                    Text(
                        text = "Filtrar por Recurso",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(TipoRecursoFiltro.values().toList()) { filtro ->
                            FilterChip(
                                selected = filtroTipo == filtro,
                                onClick = { filtroTipo = filtro },
                                label = { Text(filtro.label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colorCursoActivo,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Solicitudes Pendientes • ${asignaturaEnum.displayName}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                if (pendientesMateria.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎉 No hay solicitudes de ${filtroTipo.label.lowercase()}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "No se registran contenidos pendientes para ${asignaturaEnum.displayName} en $cursoSeleccionado con el filtro '${filtroTipo.label}'.",
                                    fontSize = 12.sp,
                                    color = UtpTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(pendientesMateria) { unidad ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Unidad ${unidad.numero}: ${unidad.titulo}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpTextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = UtpBadgeRedBg
                                    ) {
                                        Text(
                                            text = "PENDIENTE • $cursoSeleccionado",
                                            color = UtpBadgeRedText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = unidad.descripcion, fontSize = 12.sp, color = UtpTextSecondary)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.APROBADO) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = UtpBadgeGreenText)
                                    ) {
                                        Text("Aprobar", fontSize = 11.sp, color = Color.White)
                                    }

                                    OutlinedButton(
                                        onClick = { unidadARechazar = unidad },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Rechazar", fontSize = 11.sp, color = UtpBadgeRedText)
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        unidadARechazar?.let { unidad ->
            AlertDialog(
                onDismissRequest = { unidadARechazar = null },
                title = { Text("Rechazar Contenido") },
                text = {
                    Column {
                        Text("Indique las observaciones pedagógicas para la corrección:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = textoObservacion,
                            onValueChange = { textoObservacion = it },
                            placeholder = { Text("Ej: Adaptar objetivos para $cursoSeleccionado...") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (textoObservacion.isNotBlank()) {
                                UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.RECHAZADO, textoObservacion)
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
                    TextButton(onClick = { unidadARechazar = null }) { Text("Cancelar") }
                }
            )
        }
    }
}