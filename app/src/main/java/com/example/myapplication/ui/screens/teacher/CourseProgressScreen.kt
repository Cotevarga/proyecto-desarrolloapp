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
import com.example.myapplication.data.model.EstudianteAvance
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseProgressScreen(
    onBack: () -> Unit = {},
    onSelectStudentReport: (EstudianteAvance) -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    var searchQuery by remember { mutableStateOf("") }
    var cursoFiltro by remember { mutableStateOf("Todos") }
    var estudianteSeleccionado by remember { mutableStateOf<EstudianteAvance?>(null) }

    val docenteActivo = remember { repository.getDocenteById("doc_1") }
    val todosLosEstudiantes = remember(docenteActivo.id) { repository.getEstudiantesPorDocente(docenteActivo.id) }

    val opcionesCursos = remember(docenteActivo) {
        if (docenteActivo.seccionesAsignadas.size > 1) {
            listOf("Todos") + docenteActivo.seccionesAsignadas
        } else {
            docenteActivo.seccionesAsignadas
        }
    }

    val sugerenciasAutocompletado = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else todosLosEstudiantes
            .map { it.nombre }
            .filter { it.contains(searchQuery, ignoreCase = true) }
            .distinct()
            .take(4)
    }

    val estudiantesFiltrados = remember(searchQuery, cursoFiltro) {
        todosLosEstudiantes.filter { est ->
            val coincideBusqueda = searchQuery.isBlank() ||
                    est.nombre.contains(searchQuery, ignoreCase = true) ||
                    est.rut.contains(searchQuery, ignoreCase = true)

            val coincideCurso = cursoFiltro == "Todos" || est.curso == cursoFiltro

            coincideBusqueda && coincideCurso
        }
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
                    Text("Alumnos y Progreso (${docenteActivo.materiaPrincipal})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                    Text("Docente: ${docenteActivo.nombre} • ${docenteActivo.seccionesAsignadas.joinToString(", ")}", fontSize = 12.sp, color = UtpTextSecondary)
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("OA1: Contar y representar del 1 al 10", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                Surface(shape = RoundedCornerShape(6.dp), color = UtpPurpleSoft) {
                                    Text("75% Avance", fontSize = 11.sp, color = UtpPurplePrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { 0.75f },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = UtpPurplePrimary,
                                trackColor = UtpPurpleSoft
                            )
                        }
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar estudiante por nombre o RUT...", fontSize = 12.sp) },
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

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Filtrar por Curso Asignado", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            opcionesCursos.forEach { curso ->
                                val isSelected = cursoFiltro == curso
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { cursoFiltro = curso },
                                    label = { Text(curso, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = UtpPurplePrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Estudiantes (${estudiantesFiltrados.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = UtpTextPrimary
                    )
                }

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
                                Icon(Icons.Default.PersonSearch, contentDescription = null, tint = UtpTextSecondary, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No se encontraron estudiantes.", fontSize = 12.sp, color = UtpTextSecondary)
                            }
                        }
                    }
                } else {
                    items(estudiantesFiltrados) { est ->
                        val (badgeBg, badgeText) = when (est.estadoDesafios.lowercase()) {
                            "finalizado" -> UtpBadgeGreenBg to UtpBadgeGreenText
                            "pendiente" -> UtpBadgeRedBg to UtpBadgeRedText
                            else -> UtpBadgeBlueBg to UtpBadgeBlueText
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { estudianteSeleccionado = est },
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
                                    Text("${est.desafiosCompletados} • ${est.pruebaPuntaje}", fontSize = 11.sp, color = UtpTextSecondary)
                                }

                                Surface(shape = RoundedCornerShape(6.dp), color = badgeBg) {
                                    Text(
                                        text = est.estadoDesafios,
                                        color = badgeText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
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

        estudianteSeleccionado?.let { est ->
            AlertDialog(
                onDismissRequest = { estudianteSeleccionado = null },
                title = { Text("Respuestas: ${est.nombre} (${est.curso})") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Rendimiento en Prueba Final (OA1):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextSecondary)

                        if (est.preguntasDetalle.isEmpty()) {
                            Text("El estudiante aún no rinde la prueba final.", fontSize = 12.sp, color = UtpBadgeRedText)
                        } else {
                            est.preguntasDetalle.forEach { (pregunta, correcta) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(pregunta, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (correcta) UtpBadgeGreenBg else UtpBadgeRedBg
                                    ) {
                                        Text(
                                            text = if (correcta) "✓ Correcta" else "✗ Incorrecta",
                                            fontSize = 10.sp,
                                            color = if (correcta) UtpBadgeGreenText else UtpBadgeRedText,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val temp = estudianteSeleccionado
                            estudianteSeleccionado = null
                            if (temp != null) onSelectStudentReport(temp)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Text("Ver Informe Pedagógico")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { estudianteSeleccionado = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}