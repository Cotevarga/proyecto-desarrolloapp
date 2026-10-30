package com.example.myapplication.ui.screens.teacher

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.ContenidoBiblioteca
import com.example.myapplication.data.model.SolicitudPermiso
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherLibraryScreen(
    onBack: () -> Unit = {},
    onSelectObjective: (ContenidoBiblioteca) -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var unidadFiltro by remember { mutableStateOf("Todas") }

    // Estados para el Modal de Solicitar Permiso
    var mostrarModalCrear by remember { mutableStateOf(false) }
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevoTipo by remember { mutableStateOf("Cambio de Planificación") }
    var nuevoMotivo by remember { mutableStateOf("") }

    val docenteActivo = remember { repository.getDocenteById("doc_1") }

    val unidadesMateria = remember {
        listOf(
            ContenidoBiblioteca(
                id = "u1",
                asignatura = docenteActivo.materiaPrincipal,
                unidadCodigo = "Unidad 1",
                titulo = "Contamos y representamos del 1 al 10",
                descripcion = "Conteo progresivo, asociación de cantidades, guías de trazado e iniciación numérica.",
                actividadesConteo = 3,
                desafiosConteo = 2,
                pruebaConteo = 1,
                multimediaConteo = 4,
                icon = Icons.Default.Percent
            ),
            ContenidoBiblioteca(
                id = "u2",
                asignatura = docenteActivo.materiaPrincipal,
                unidadCodigo = "Unidad 2",
                titulo = "Adición y Sustracción Inicial (hasta el 10)",
                descripcion = "Estrategias concretas de juntar, agregar, quitar y descomponer números hasta el 10.",
                actividadesConteo = 5,
                desafiosConteo = 3,
                pruebaConteo = 1,
                multimediaConteo = 6,
                icon = Icons.Default.Calculate
            ),
            ContenidoBiblioteca(
                id = "u3",
                asignatura = docenteActivo.materiaPrincipal,
                unidadCodigo = "Unidad 3",
                titulo = "Formas Geométricas y Entorno 2D/3D",
                descripcion = "Identificación de figuras planas y cuerpos geométricos cotidianos en la sala de clases.",
                actividadesConteo = 4,
                desafiosConteo = 2,
                pruebaConteo = 1,
                multimediaConteo = 3,
                icon = Icons.Default.Category
            ),
            ContenidoBiblioteca(
                id = "u4",
                asignatura = docenteActivo.materiaPrincipal,
                unidadCodigo = "Unidad 4",
                titulo = "Patrones, Secuencias y Medición no estandarizada",
                descripcion = "Reconocimiento de secuencias repetitivas y comparación directa de longitudes.",
                actividadesConteo = 3,
                desafiosConteo = 2,
                pruebaConteo = 1,
                multimediaConteo = 2,
                icon = Icons.Default.Straighten
            )
        )
    }

    val listaFiltrada = unidadesMateria.filter { unit ->
        val coincideUnidad = unidadFiltro == "Todas" || unit.unidadCodigo == unidadFiltro
        val coincideBusqueda = searchQuery.isBlank() ||
                unit.titulo.contains(searchQuery, ignoreCase = true) ||
                unit.unidadCodigo.contains(searchQuery, ignoreCase = true) ||
                unit.descripcion.contains(searchQuery, ignoreCase = true)

        coincideUnidad && coincideBusqueda
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
                        Text("Unidades de ${docenteActivo.materiaPrincipal}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                        Text("1° Año Básico • Unidades pedagógicas autorizadas por UTP", fontSize = 12.sp, color = UtpTextSecondary)
                    }
                }

                // Buscador
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar por unidad, tema o palabra clave...", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = UtpPurplePrimary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = UtpPurplePrimary
                        ),
                        singleLine = true
                    )
                }

                // Filtro Horizontal por Unidades
                item {
                    val unidadesList = listOf("Todas", "Unidad 1", "Unidad 2", "Unidad 3", "Unidad 4")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(unidadesList) { u ->
                            FilterChip(
                                selected = unidadFiltro == u,
                                onClick = { unidadFiltro = u },
                                label = { Text(u, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = UtpPurplePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Tarjetas de Unidades
                items(listaFiltrada) { item ->
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = UtpPurpleSoft,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(item.icon, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(item.asignatura, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                                }

                                Surface(shape = RoundedCornerShape(6.dp), color = UtpBadgeGreenBg) {
                                    Text("✓ Autorizado UTP", color = UtpBadgeGreenText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("${item.unidadCodigo}:${item.titulo}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.descripcion, fontSize = 12.sp, color = UtpTextSecondary)

                            Spacer(modifier = Modifier.height(14.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    Surface(shape = RoundedCornerShape(6.dp), color = UtpPurpleSoft) {
                                        Text("${item.actividadesConteo} Actividades", fontSize = 10.sp, color = UtpPurplePrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                    }
                                }
                                item {
                                    Surface(shape = RoundedCornerShape(6.dp), color = UtpBadgeBlueBg) {
                                        Text("${item.desafiosConteo} Desafíos", fontSize = 10.sp, color = UtpBadgeBlueText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                    }
                                }
                                item {
                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFEF3C7)) {
                                        Text("${item.pruebaConteo} Prueba", fontSize = 10.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                    }
                                }
                                item {
                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFD1FAE5)) {
                                        Text("${item.multimediaConteo} Multimedia", fontSize = 10.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { onSelectObjective(item) },
                                    colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text("Seleccionar Unidad", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        // --- BOTÓN FLOTANTE (FAB) "Solicitar Permiso" EN PANTALLA CLASES ---
        ExtendedFloatingActionButton(
            onClick = { mostrarModalCrear = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 16.dp),
            containerColor = UtpPurplePrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = "Añadir") },
            text = { Text("Solicitar Permiso", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        )

        // --- DIÁLOGO MODAL PARA SOLICITAR PERMISO ---
        if (mostrarModalCrear) {
            AlertDialog(
                onDismissRequest = { mostrarModalCrear = false },
                title = {
                    Text(
                        text = "Solicitar Permiso a UTP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = nuevoTitulo,
                            onValueChange = { nuevoTitulo = it },
                            label = { Text("Título de la solicitud", fontSize = 12.sp) },
                            placeholder = { Text("Ej: Evaluación Diferida 1° A", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        var expandirDropdown by remember { mutableStateOf(false) }
                        val tiposPermisos = listOf(
                            "Cambio de Planificación",
                            "Evaluación Diferida",
                            "Ajuste de OA",
                            "Permiso Administrativo"
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandirDropdown,
                            onExpandedChange = { expandirDropdown = !expandirDropdown }
                        ) {
                            OutlinedTextField(
                                value = nuevoTipo,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tipo de trámite", fontSize = 12.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirDropdown) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandirDropdown,
                                onDismissRequest = { expandirDropdown = false }
                            ) {
                                tiposPermisos.forEach { tipo ->
                                    DropdownMenuItem(
                                        text = { Text(tipo, fontSize = 12.sp) },
                                        onClick = {
                                            nuevoTipo = tipo
                                            expandirDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = nuevoMotivo,
                            onValueChange = { nuevoMotivo = it },
                            label = { Text("Motivo o Detalle pedagógico", fontSize = 12.sp) },
                            placeholder = { Text("Explica la razón de tu solicitud...", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nuevoTitulo.isNotBlank() && nuevoMotivo.isNotBlank()) {
                                val nueva = SolicitudPermiso(
                                    id = "sol_${System.currentTimeMillis()}",
                                    titulo = nuevoTitulo,
                                    tipoPermiso = nuevoTipo,
                                    fechaSolicitud = "30 Oct 2026",
                                    estado = "PENDIENTE",
                                    motivoDocente = nuevoMotivo,
                                    respuestaUtp = "Solicitud ingresada desde Clases. En espera de revisión por UTP."
                                )
                                repository.agregarSolicitud(nueva)
                                mostrarModalCrear = false
                                nuevoTitulo = ""
                                nuevoMotivo = ""
                                Toast.makeText(context, "Solicitud enviada a Jefatura UTP. Revisa el estado en la pestaña Permisos.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Solicitar Permiso", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarModalCrear = false }) {
                        Text("Cancelar", color = UtpTextSecondary)
                    }
                }
            )
        }
    }
}