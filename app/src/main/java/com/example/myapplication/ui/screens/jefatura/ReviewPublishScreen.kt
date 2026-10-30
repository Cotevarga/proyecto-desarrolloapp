package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.myapplication.data.model.AsignaturaUTP
import com.example.myapplication.data.model.EstadoContenido
import com.example.myapplication.data.model.UnidadPedagogica
import com.example.myapplication.data.repository.UtpRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

enum class FiltroMuroTipo(val label: String, val icon: ImageVector) {
    TODOS("Todos", Icons.Default.AllInclusive),
    UNIDADES("Unidades", Icons.Default.Book),
    ACTIVIDADES("Actividades", Icons.Default.Assignment),
    DESAFIOS("Desafíos", Icons.Default.EmojiEvents)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPublishScreen(
    onBack: () -> Unit = {},
    onEditUnit: () -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val unidades by UtpRepository.unidadesState.collectAsState()

    var cursoFiltro by remember { mutableStateOf("Todos") }
    var materiaFiltro by remember { mutableStateOf<AsignaturaUTP?>(null) }
    var tipoFiltro by remember { mutableStateOf(FiltroMuroTipo.TODOS) }
    var textoBusqueda by remember { mutableStateOf("") }

    var unidadDetalleSeleccionada by remember { mutableStateOf<UnidadPedagogica?>(null) }

    val muroPublicaciones = unidades.filter { unidad ->
        val coincideCurso = when (cursoFiltro) {
            "1° A" -> !unidad.titulo.contains("1° B", true) && !unidad.descripcion.contains("1° B", true)
            "1° B" -> unidad.titulo.contains("1° B", true) || unidad.descripcion.contains("1° B", true)
            else -> true
        }

        val coincideMateria = materiaFiltro == null || unidad.asignatura == materiaFiltro

        val coincideTipo = when (tipoFiltro) {
            FiltroMuroTipo.TODOS -> true
            FiltroMuroTipo.UNIDADES -> unidad.numero > 0 || unidad.titulo.contains("Unidad", true)
            FiltroMuroTipo.ACTIVIDADES -> unidad.titulo.contains("Actividad", true) || unidad.descripcion.contains("Actividad", true) || unidad.titulo.contains("Guía", true)
            FiltroMuroTipo.DESAFIOS -> unidad.titulo.contains("Desafío", true) || unidad.descripcion.contains("Desafío", true)
        }

        val coincideTexto = textoBusqueda.isBlank() ||
                unidad.titulo.contains(textoBusqueda, ignoreCase = true) ||
                unidad.descripcion.contains(textoBusqueda, ignoreCase = true)

        coincideCurso && coincideMateria && coincideTipo && coincideTexto
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userName = "Prof. Carolina M.",
                userRole = "Jefatura de UTP",
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
                item { Spacer(modifier = Modifier.height(4.dp)) }

                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            listOf("Todos", "1° A", "1° B").forEach { curso ->
                                val isSelected = cursoFiltro == curso
                                val containerColor = when {
                                    !isSelected -> Color.Transparent
                                    curso == "1° A" -> UtpOrangeHighlight
                                    else -> UtpPurplePrimary
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(containerColor)
                                        .clickable { cursoFiltro = curso },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = curso,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else UtpTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MATERIAS (${AsignaturaUTP.values().size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextSecondary
                        )
                        Text(
                            text = if (materiaFiltro != null) "Ver todas" else "Filtradas",
                            fontSize = 12.sp,
                            color = UtpPurplePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { materiaFiltro = null }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(AsignaturaUTP.values().toList()) { asig ->
                            val isSelected = materiaFiltro == asig
                            val cantidadPub = unidades.count { unidad ->
                                val esMismaMateria = unidad.asignatura == asig
                                val coincideCurso = when (cursoFiltro) {
                                    "1° A" -> !unidad.titulo.contains("1° B", true) && !unidad.descripcion.contains("1° B", true)
                                    "1° B" -> unidad.titulo.contains("1° B", true) || unidad.descripcion.contains("1° B", true)
                                    else -> true
                                }
                                esMismaMateria && coincideCurso
                            }

                            Card(
                                modifier = Modifier.clickable { materiaFiltro = if (isSelected) null else asig },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) UtpPurpleSoft else UtpCardBackground
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) UtpPurplePrimary else UtpBadgeBlueBg,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = getIconForAsignatura(asig),
                                                contentDescription = null,
                                                tint = if (isSelected) Color.White else UtpBadgeBlueText,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(asig.displayName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                        Text("$cantidadPub pub.", fontSize = 10.sp, color = UtpTextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(FiltroMuroTipo.values().toList()) { tipo ->
                            FilterChip(
                                selected = tipoFiltro == tipo,
                                onClick = { tipoFiltro = tipo },
                                label = { Text(tipo.label, fontSize = 12.sp) },
                                leadingIcon = { Icon(tipo.icon, contentDescription = null, modifier = Modifier.size(14.dp)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = UtpPurplePrimary,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White
                                )
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        placeholder = { Text("Buscar publicaciones, temas...", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = UtpTextSecondary) },
                        trailingIcon = {
                            if (textoBusqueda.isNotEmpty()) {
                                IconButton(onClick = { textoBusqueda = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                }

                if (muroPublicaciones.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = UtpCardBackground)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(36.dp), tint = UtpTextSecondary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No hay publicaciones en este filtro", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                Text("Intenta cambiando el paralelo, asignatura o la búsqueda.", fontSize = 12.sp, color = UtpTextSecondary)
                            }
                        }
                    }
                } else {
                    items(muroPublicaciones) { unidad ->
                        val esDeshabilitado = unidad.estado == EstadoContenido.DESHABILITADO
                        MuroPostCard(
                            unidad = unidad,
                            onVerDetalle = { unidadDetalleSeleccionada = unidad },
                            onEditar = onEditUnit,
                            onToggleHabilitar = {
                                if (esDeshabilitado) {
                                    UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.PUBLICADO, "Habilitado desde el muro")
                                } else {
                                    UtpRepository.cambiarEstadoUnidad(unidad.id, EstadoContenido.DESHABILITADO, "Deshabilitado desde el muro")
                                }
                            }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        FloatingActionButton(
            onClick = onEditUnit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp),
            containerColor = UtpPurplePrimary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Crear Contenido")
        }

        unidadDetalleSeleccionada?.let { unidad ->
            AlertDialog(
                onDismissRequest = { unidadDetalleSeleccionada = null },
                title = { Text("Detalle de Publicación") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(unidad.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Asignatura: ${unidad.asignatura.displayName}", fontSize = 12.sp, color = UtpTextSecondary)
                        Text("Estado: ${unidad.estado.name}", fontSize = 12.sp, color = UtpPurplePrimary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(unidad.descripcion, fontSize = 13.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { unidadDetalleSeleccionada = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Composable
fun MuroPostCard(
    unidad: UnidadPedagogica,
    onVerDetalle: () -> Unit,
    onEditar: () -> Unit,
    onToggleHabilitar: () -> Unit
) {
    val esDeshabilitado = unidad.estado == EstadoContenido.DESHABILITADO

    val cursoDetectado = remember(unidad) {
        if (unidad.titulo.contains("1° B", ignoreCase = true) || unidad.descripcion.contains("1° B", ignoreCase = true)) {
            "1° B"
        } else {
            "1° A"
        }
    }
    val colorBadgeCurso = if (cursoDetectado == "1° B") UtpPurplePrimary else UtpOrangeHighlight

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(shape = RoundedCornerShape(6.dp), color = colorBadgeCurso) {
                        Text(cursoDetectado, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = UtpBadgeBlueBg) {
                        Text("📖 ${unidad.asignatura.displayName}", color = UtpBadgeBlueText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                Surface(shape = RoundedCornerShape(6.dp), color = if (esDeshabilitado) UtpBadgeRedBg else UtpPurpleSoft) {
                    Text(
                        text = if (esDeshabilitado) "🚫 DESHABILITADO" else "📖 Unidad",
                        color = if (esDeshabilitado) UtpBadgeRedText else UtpPurplePrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Unidad ${unidad.numero}: ${unidad.titulo}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(12.dp), tint = UtpTextSecondary)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fecha: 5 Nov 2026", fontSize = 11.sp, color = UtpTextSecondary)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = unidad.descripcion,
                fontSize = 12.sp,
                color = UtpTextSecondary,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable(onClick = onVerDetalle),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver detalle", fontSize = 12.sp, color = UtpPurplePrimary, fontWeight = FontWeight.Bold)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onEditar, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = UtpTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onToggleHabilitar, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (esDeshabilitado) Icons.Default.CheckCircle else Icons.Default.Block,
                            contentDescription = if (esDeshabilitado) "Habilitar" else "Deshabilitar",
                            tint = if (esDeshabilitado) UtpBadgeGreenText else UtpBadgeRedText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getIconForAsignatura(asignatura: AsignaturaUTP): ImageVector {
    return when (asignatura) {
        AsignaturaUTP.MATEMATICAS -> Icons.Default.Percent
        AsignaturaUTP.LENGUAJE -> Icons.Default.Book
        AsignaturaUTP.HISTORIA -> Icons.Default.MenuBook
        AsignaturaUTP.CIENCIAS -> Icons.Default.Science
    }
}