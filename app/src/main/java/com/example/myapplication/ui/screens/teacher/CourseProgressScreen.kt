package com.example.myapplication.ui.screens.teacher

import androidx.compose.foundation.BorderStroke
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
    onAccountSettings: () -> Unit = {}, // <-- Parámetro agregado
    onLogout: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    var cursoFiltro by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf("") }

    // Modal de Avance del Estudiante
    var estudianteSeleccionadoModal by remember { mutableStateOf<EstudianteAvance?>(null) }

    val docenteActivo = remember { repository.getDocenteById("doc_1") }
    val listaEstudiantes = remember { repository.getEstudiantesPorDocente("doc_1") }

    val estudiantesFiltrados = listaEstudiantes.filter { est ->
        val coincideCurso = cursoFiltro == "Todos" || est.curso == cursoFiltro
        val coincideBusqueda = searchQuery.isBlank() ||
                est.nombre.contains(searchQuery, ignoreCase = true) ||
                est.rut.contains(searchQuery, ignoreCase = true)
        coincideCurso && coincideBusqueda
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                showBack = false,
                onAccountSettings = onAccountSettings, // <-- Conectado
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
                        Text(
                            text = "Seguimiento de Alumnos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Text(
                            text = "Avance individual y rendimiento en ${docenteActivo.materiaPrincipal}",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar por alumno o RUT...", fontSize = 12.sp) },
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
                    val cursosFiltroList = listOf("Todos", "1° A", "1° B")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(cursosFiltroList) { c ->
                            FilterChip(
                                selected = cursoFiltro == c,
                                onClick = { cursoFiltro = c },
                                label = { Text(c, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = UtpPurplePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
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

                items(estudiantesFiltrados) { estudiante ->
                    val esFinalizado = estudiante.estadoDesafios == "Finalizado"
                    val progreso = when {
                        estudiante.desafiosCompletados.startsWith("3/3") -> 1.0f
                        estudiante.desafiosCompletados.startsWith("2/3") -> 0.66f
                        estudiante.desafiosCompletados.startsWith("1/3") -> 0.33f
                        else -> 0.0f
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = UtpPurpleSoft,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = estudiante.nombre.take(1),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = UtpPurplePrimary
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = estudiante.nombre,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = UtpTextPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = if (estudiante.curso == "1° B") UtpPurplePrimary else UtpOrangeHighlight
                                            ) {
                                                Text(
                                                    text = estudiante.curso,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "RUT: ${estudiante.rut}",
                                            fontSize = 11.sp,
                                            color = UtpTextSecondary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (esFinalizado) UtpBadgeGreenBg else Color(0xFFFEE2E2)
                                ) {
                                    Text(
                                        text = estudiante.estadoDesafios,
                                        color = if (esFinalizado) UtpBadgeGreenText else Color(0xFFDC2626),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Avance: ${estudiante.desafiosCompletados}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = UtpTextSecondary
                                    )
                                    Text(
                                        text = "${(progreso * 100).toInt()}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpPurplePrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { progreso },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp),
                                    color = UtpPurplePrimary,
                                    trackColor = Color(0xFFE2E8F0),
                                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { estudianteSeleccionadoModal = estudiante },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = UtpPurplePrimary),
                                    border = BorderStroke(1.dp, UtpPurplePrimary),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ver Avance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { onSelectStudentReport(estudiante) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ver Informe", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        estudianteSeleccionadoModal?.let { est ->
            AlertDialog(
                onDismissRequest = { estudianteSeleccionadoModal = null },
                title = {
                    Text(
                        text = "Respuestas: ${est.nombre} (${est.curso})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Rendimiento en Prueba Final (OA1):",
                            fontSize = 12.sp,
                            color = Color(0xFFE2E8F0)
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF2F2),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Aún no se implementa la base de datos",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val alumno = est
                            estudianteSeleccionadoModal = null
                            onSelectStudentReport(alumno)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ver Informe Pedagógico", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { estudianteSeleccionadoModal = null }) {
                        Text("Cerrar", fontSize = 12.sp, color = Color.LightGray)
                    }
                }
            )
        }
    }
}