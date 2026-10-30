package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.ContenidoBiblioteca
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

data class RecursoDetalle(
    val id: String,
    val titulo: String,
    val tipo: String, // "ACTIVIDAD", "DESAFIO", "PRUEBA", "MULTIMEDIA"
    val detalle: String,
    var diasDisponible: Int = 7
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherLibraryScreen(
    onBack: () -> Unit = {},
    onSelectObjective: (ContenidoBiblioteca) -> Unit = {},
    onRequestPermission: () -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    var searchQuery by remember { mutableStateOf("") }
    var unidadFiltro by remember { mutableStateOf("Todas") }

    // Modal principal de recursos
    var unidadDetalleSeleccionada by remember { mutableStateOf<ContenidoBiblioteca?>(null) }

    // Modal secundario para editar días de disponibilidad
    var recursoAEditar by remember { mutableStateOf<RecursoDetalle?>(null) }
    var diasSeleccionadosTemp by remember { mutableStateOf(7) }

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

    fun getCursosAsignados(unidadId: String): List<String> {
        return when (unidadId) {
            "u1" -> listOf("1° Año A")
            "u2" -> listOf("1° Año A", "1° Año B")
            "u3" -> listOf("1° Año B")
            else -> emptyList()
        }
    }

    val mapaRecursos = remember {
        mutableStateMapOf<String, MutableList<RecursoDetalle>>(
            "u1" to mutableStateListOf(
                RecursoDetalle("r1", "Guía N°1: Trazado del 1 al 5", "ACTIVIDAD", "Ficha impresa interactiva con imágenes.", 7),
                RecursoDetalle("r2", "Guía N°2: Conteo de elementos", "ACTIVIDAD", "Completar cantidades y emparejar números.", 5),
                RecursoDetalle("r3", "Desafío Rápido: La recta numérica", "DESAFIO", "Encontrar el número que falta antes del 10.", 3),
                RecursoDetalle("r4", "Evaluación Sumativa N°1", "PRUEBA", "Prueba escrita individual (15 ítems).", 10),
                RecursoDetalle("r5", "Video: Los Números Cantando", "MULTIMEDIA", "Video MP4 animado (3 min).", 15)
            ),
            "u2" to mutableStateListOf(
                RecursoDetalle("r6", "Guía N°1: Juntando elementos concretos", "ACTIVIDAD", "Sumas sencillas con dibujos.", 7),
                RecursoDetalle("r7", "Guía N°2: Quitando unidades", "ACTIVIDAD", "Sustracciones del 1 al 5.", 7),
                RecursoDetalle("r8", "Guía N°3: Descomposición numérica", "ACTIVIDAD", "Fichas de dominó numéricas.", 5),
                RecursoDetalle("r9", "Guía N°4: Problemas ilustrados", "ACTIVIDAD", "Resolución de problemas cotidianos.", 7),
                RecursoDetalle("r10", "Guía N°5: Cálculo mental inicial", "ACTIVIDAD", "Ejercicios breves cronometrados.", 4)
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
                        Text("Unidades pedagógicas autorizadas por UTP y asignadas a tus cursos", fontSize = 12.sp, color = UtpTextSecondary)
                    }
                }

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

                items(listaFiltrada) { item ->
                    val cursosAsignados = getCursosAsignados(item.id)

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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = UtpPurpleSoft,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(item.icon, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Text(item.asignatura, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)

                                    CursoBadgesComponent(cursos = cursosAsignados)
                                }

                                Surface(shape = RoundedCornerShape(6.dp), color = UtpBadgeGreenBg) {
                                    Text("✓ Autorizado UTP", color = UtpBadgeGreenText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("${item.unidadCodigo}: ${item.titulo}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
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
                                    onClick = { unidadDetalleSeleccionada = item },
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

        ExtendedFloatingActionButton(
            onClick = { onRequestPermission() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 16.dp),
            containerColor = UtpPurplePrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = "Añadir") },
            text = { Text("Solicitar Permiso", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        )

        // --- DIÁLOGO MODAL ---
        unidadDetalleSeleccionada?.let { unidad ->
            val listaRecursos = mapaRecursos.getOrPut(unidad.id) {
                mutableStateListOf(
                    RecursoDetalle("r_gen1", "Guía Complementaria N°1", "ACTIVIDAD", "Ejercicios genéricos de la unidad.", 7),
                    RecursoDetalle("r_gen2", "Desafío Semanal", "DESAFIO", "Actividad interactiva.", 5)
                )
            }
            val cursosAsignados = getCursosAsignados(unidad.id)

            AlertDialog(
                onDismissRequest = { unidadDetalleSeleccionada = null },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${unidad.unidadCodigo}: Recurso Publicado",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            CursoBadgesComponent(cursos = cursosAsignados)
                        }

                        Text(
                            text = unidad.titulo,
                            fontSize = 13.sp,
                            color = Color(0xFFC7D2FE),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Toca cualquier contenido para cambiar los días de disponibilidad:",
                            fontSize = 11.sp,
                            color = Color(0xFFE2E8F0)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(listaRecursos) { rec ->
                                val (bgBadge, textBadge) = when (rec.tipo) {
                                    "ACTIVIDAD" -> Pair(UtpPurpleSoft, UtpPurplePrimary)
                                    "DESAFIO" -> Pair(UtpBadgeBlueBg, UtpBadgeBlueText)
                                    "PRUEBA" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
                                    else -> Pair(Color(0xFFD1FAE5), Color(0xFF059669))
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            recursoAEditar = rec
                                            diasSeleccionadosTemp = rec.diasDisponible
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = bgBadge
                                        ) {
                                            Text(
                                                text = rec.tipo,
                                                color = textBadge,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(rec.titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                            Text(rec.detalle, fontSize = 10.sp, color = UtpTextSecondary)
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = UtpPurpleSoft
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Timer, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text("${rec.diasDisponible}d", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val item = unidad
                            unidadDetalleSeleccionada = null
                            onSelectObjective(item)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Editar Unidad", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { unidadDetalleSeleccionada = null }) {
                        Text("Cerrar", fontSize = 12.sp, color = Color.LightGray)
                    }
                }
            )
        }

        // --- MODAL SECUNDARIO: EDITAR DÍAS DISPONIBLES ---
        recursoAEditar?.let { rec ->
            AlertDialog(
                onDismissRequest = { recursoAEditar = null },
                title = {
                    Text("Configurar Disponibilidad", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(rec.titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
                        Text("Indica cuántos días estará disponible este contenido para los estudiantes:", fontSize = 12.sp, color = UtpTextSecondary)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF7F7FA), RoundedCornerShape(12.dp))
                                .padding(vertical = 8.dp)
                        ) {
                            IconButton(onClick = { if (diasSeleccionadosTemp > 1) diasSeleccionadosTemp-- }) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Restar", tint = UtpPurplePrimary)
                            }

                            Text(
                                text = "$diasSeleccionadosTemp días",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = UtpTextPrimary,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            IconButton(onClick = { diasSeleccionadosTemp++ }) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = "Sumar", tint = UtpPurplePrimary)
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(3, 7, 14, 30).forEach { dias ->
                                FilterChip(
                                    selected = diasSeleccionadosTemp == dias,
                                    onClick = { diasSeleccionadosTemp = dias },
                                    label = { Text("$dias d", fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = UtpPurplePrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            rec.diasDisponible = diasSeleccionadosTemp
                            recursoAEditar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Guardar Disponibilidad", fontSize = 12.sp)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { recursoAEditar = null }) {
                        Text("Cancelar", fontSize = 12.sp)
                    }
                }
            )
        }
    }
}

@Composable
fun CursoBadgesComponent(cursos: List<String>) {
    when {
        cursos.isEmpty() -> {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            ) {
                Text(
                    text = "Sin Asignar",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        cursos.size > 1 -> {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = UtpPurplePrimary
            ) {
                Text(
                    text = "1° A y 1° B",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        else -> {
            val curso = cursos.first()
            val badgeColor = if (curso.contains("1° Año B") || curso.contains("1° B")) UtpPurplePrimary else UtpOrangeHighlight
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = badgeColor
            ) {
                Text(
                    text = curso,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}