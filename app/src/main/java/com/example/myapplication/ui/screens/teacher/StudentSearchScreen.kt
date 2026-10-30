package com.example.myapplication.ui.screens.teacher

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

data class EstudianteBuscador(
    val id: String,
    val nombre: String,
    val rut: String,
    val curso: String,
    val estadoEvaluacion: String, // "Rendida", "Pendiente", "En Curso"
    val rendimientoOA: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSearchScreen(
    onBack: () -> Unit = {},
    onSelectStudent: (EstudianteBuscador) -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var cursoFiltro by remember { mutableStateOf("Todos") }
    var estadoFiltro by remember { mutableStateOf("Todos") }

    // Lista ficticia de estudiantes para 1° Básico con nombres que inician / contienen la letra "C" y otras
    val todosLosEstudiantes = remember {
        listOf(
            EstudianteBuscador("1", "Camila Morales", "24.123.456-1", "1° A", "Rendida", "80% (4/5)"),
            EstudianteBuscador("2", "Carlos Silva", "24.234.567-2", "1° A", "Pendiente", "Sin rendir"),
            EstudianteBuscador("3", "Carolina Fuentes", "24.345.678-3", "1° B", "Rendida", "100% (5/5)"),
            EstudianteBuscador("4", "Cristian Vera", "24.456.789-4", "1° A", "En Curso", "Desafío 2/3"),
            EstudianteBuscador("5", "Constanza Henríquez", "24.567.890-5", "1° B", "Rendida", "60% (3/5)"),
            EstudianteBuscador("6", "Lucas Morales", "24.678.901-6", "1° A", "Rendida", "80% (4/5)"),
            EstudianteBuscador("7", "Mateo Silva", "24.789.012-7", "1° A", "Rendida", "100% (5/5)"),
            EstudianteBuscador("8", "Sofía Henríquez", "24.890.123-8", "1° B", "Pendiente", "Sin rendir")
        )
    }

    // Sugerencias de Autocompletado (nombres sugeridos en chips dinámicos según lo escrito)
    val sugerenciasAutocompletado = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else todosLosEstudiantes
            .map { it.nombre }
            .filter { it.contains(searchQuery, ignoreCase = true) }
            .take(4)
    }

    // Filtrado en tiempo real según búsqueda, curso y estado
    val estudiantesFiltrados = remember(searchQuery, cursoFiltro, estadoFiltro) {
        todosLosEstudiantes.filter { est ->
            val coincideBusqueda = searchQuery.isBlank() ||
                    est.nombre.contains(searchQuery, ignoreCase = true) ||
                    est.rut.contains(searchQuery, ignoreCase = true)

            val coincideCurso = cursoFiltro == "Todos" || est.curso == cursoFiltro
            val coincideEstado = estadoFiltro == "Todos" || est.estadoEvaluacion.equals(estadoFiltro, ignoreCase = true)

            coincideBusqueda && coincideCurso && coincideEstado
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                showBack = true,
                onBack = onBack,
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
                    Text("Buscador de Estudiantes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Text("Búsqueda con autocompletado y filtros de curso", fontSize = 12.sp, color = UtpTextSecondary)
                }

                // Campo de Búsqueda con Autocompletado
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Escribe un nombre (ej. 'C' o 'Camila')...", fontSize = 12.sp) },
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

                        // Barra de sugerencias de autocompletado rápido
                        if (sugerenciasAutocompletado.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Sugerencias: ", fontSize = 11.sp, color = UtpTextSecondary, fontWeight = FontWeight.Bold)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(sugerenciasAutocompletado) { nombreSugerido ->
                                        SuggestionChip(
                                            onClick = { searchQuery = nombreSugerido },
                                            label = { Text(nombreSugerido, fontSize = 11.sp, color = UtpPurplePrimary) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = UtpPurpleSoft
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Filtros de Curso y Estado
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Filtros rápidos", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Filtro Cursos
                            val cursos = listOf("Todos", "1° A", "1° B")
                            items(cursos) { c ->
                                FilterChip(
                                    selected = cursoFiltro == c,
                                    onClick = { cursoFiltro = c },
                                    label = { Text("Curso: $c", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = UtpPurplePrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }

                            // Filtro Estados
                            val estados = listOf("Todos", "Rendida", "Pendiente", "En Curso")
                            items(estados) { est ->
                                FilterChip(
                                    selected = estadoFiltro == est,
                                    onClick = { estadoFiltro = est },
                                    label = { Text("Estado: $est", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = UtpBadgeBlueText,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                // Contador de Resultados
                item {
                    Text(
                        text = "Resultados encontrados (${estudiantesFiltrados.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

                // Lista de Resultados
                if (estudiantesFiltrados.isEmpty()) {
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
                                Icon(Icons.Default.PersonSearch, contentDescription = null, tint = UtpTextSecondary, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No se encontraron estudiantes con ese criterio.", fontSize = 13.sp, color = UtpTextSecondary)
                            }
                        }
                    }
                } else {
                    items(estudiantesFiltrados) { est ->
                        val (badgeBg, badgeText) = when (est.estadoEvaluacion.lowercase()) {
                            "rendida" -> UtpBadgeGreenBg to UtpBadgeGreenText
                            "pendiente" -> UtpBadgeRedBg to UtpBadgeRedText
                            else -> UtpBadgeBlueBg to UtpBadgeBlueText
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectStudent(est) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(42.dp),
                                    shape = CircleShape,
                                    color = UtpPurpleSoft
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = est.nombre.take(1),
                                            fontWeight = FontWeight.Bold,
                                            color = UtpPurplePrimary,
                                            fontSize = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(est.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(shape = RoundedCornerShape(4.dp), color = UtpPurpleSoft) {
                                            Text(est.curso, fontSize = 9.sp, color = UtpPurplePrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                        }
                                    }
                                    Text("RUT: ${est.rut} • Puntaje: ${est.rendimientoOA}", fontSize = 11.sp, color = UtpTextSecondary)
                                }

                                Surface(shape = RoundedCornerShape(6.dp), color = badgeBg) {
                                    Text(est.estadoEvaluacion, color = badgeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }

                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UtpTextSecondary)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}