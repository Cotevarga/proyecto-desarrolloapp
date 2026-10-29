package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.myapplication.ui.theme.*

// Tipos de Publicación para la Sección 3
enum class TipoPublicacionEnum(val displayName: String, val icon: ImageVector) {
    PRUEBA("Prueba", Icons.Default.Edit),
    UNIDAD("Unidad", Icons.Default.Book),
    DESAFIO("Desafío", Icons.Default.EmojiEvents),
    FOTO("Foto", Icons.Default.CameraAlt),
    AUDIO("Audio", Icons.Default.Mic),
    ACTIVIDAD("Actividad", Icons.Default.Assignment)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitEditorScreen(
    onBack: () -> Unit = {}
) {
    // Estados del formulario
    var cursoSeleccionado by remember { mutableStateOf("1° Año A") }
    var asignaturaSeleccionada by remember { mutableStateOf(AsignaturaUTP.MATEMATICAS) }
    var tipoPublicacionSeleccionado by remember { mutableStateOf(TipoPublicacionEnum.UNIDAD) }

    var tituloText by remember { mutableStateOf("") }
    var descripcionText by remember { mutableStateOf("") }

    // Estados dinámicos según el tipo de recurso
    val preguntasList = remember { mutableStateListOf<String>() }
    var nuevaPreguntaText by remember { mutableStateOf("") }

    var audioCargadoSimulado by remember { mutableStateOf<String?>(null) }
    var fotoCargadaSimulada by remember { mutableStateOf<String?>(null) }
    var oaSeleccionado by remember { mutableStateOf("OA1 - Demostrar comprensión") }

    // Limpiar formulario
    fun limpiarCampos() {
        tituloText = ""
        descripcionText = ""
        cursoSeleccionado = "1° Año A"
        asignaturaSeleccionada = AsignaturaUTP.MATEMATICAS
        tipoPublicacionSeleccionado = TipoPublicacionEnum.UNIDAD
        preguntasList.clear()
        nuevaPreguntaText = ""
        audioCargadoSimulado = null
        fotoCargadaSimulada = null
    }

    // Guardar publicación en la base de datos local
    fun guardarPublicacion() {
        if (tituloText.isNotBlank()) {
            val sufijoCurso = if (cursoSeleccionado == "1° Año B") " (1° B)" else ""
            val tituloFinal = "$tituloText$sufijoCurso"

            // Detalle descriptivo enriquecido según recurso
            val detalleEspecifico = when (tipoPublicacionSeleccionado) {
                TipoPublicacionEnum.PRUEBA, TipoPublicacionEnum.DESAFIO, TipoPublicacionEnum.ACTIVIDAD -> {
                    if (preguntasList.isNotEmpty()) " Preguntas agregadas: ${preguntasList.size} items." else ""
                }
                TipoPublicacionEnum.AUDIO -> audioCargadoSimulado?.let { " Archivo adjunto: $it" } ?: ""
                TipoPublicacionEnum.FOTO -> fotoCargadaSimulada?.let { " Imagen adjunta: $it" } ?: ""
                TipoPublicacionEnum.UNIDAD -> " Cobertura: $oaSeleccionado."
            }

            val descripcionFinal = descripcionText.ifBlank {
                "Contenido de tipo ${tipoPublicacionSeleccionado.displayName} asignado a $cursoSeleccionado."
            } + detalleEspecifico

            val nuevaUnidad = UnidadPedagogica(
                id = System.currentTimeMillis().toString(),
                numero = (UtpRepository.unidadesState.value.size + 1),
                titulo = tituloFinal,
                descripcion = descripcionFinal,
                asignatura = asignaturaSeleccionada,
                estado = EstadoContenido.PUBLICADO
            )

            UtpRepository.agregarUnidad(nuevaUnidad)
            onBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UtpWarmBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP BAR SUPERIOR
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Limpiar
                    TextButton(onClick = { limpiarCampos() }) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Limpiar", color = Color.Gray, fontSize = 14.sp)
                    }

                    // Título central
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Nueva Publicación", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = UtpTextPrimary)
                        Text("Creación de Contenido", fontSize = 11.sp, color = UtpPurplePrimary)
                    }

                    // Botón Publicar
                    Button(
                        onClick = { guardarPublicacion() },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Publicar", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // SECCIÓN 1: CURSO DESTINO
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BadgeNumber(number = "1")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Curso Destino *", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                                }
                                Text("¿Para quién es?", fontSize = 11.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                listOf("1° Año A", "1° Año B").forEach { curso ->
                                    val selected = cursoSeleccionado == curso
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .clickable { cursoSeleccionado = curso },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selected) Color(0xFFF0F0FF) else Color(0xFFF7F7FA),
                                        border = if (selected) BorderStroke(1.5.dp, UtpPurplePrimary) else null
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (selected) UtpPurplePrimary else Color.Gray)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(curso, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, color = if (selected) UtpPurplePrimary else Color.Gray, fontSize = 13.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // SECCIÓN 2: ASIGNATURA / MATERIA
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BadgeNumber(number = "2")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Asignatura / Materia *", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val asignaturasList = listOf(
                                Triple(AsignaturaUTP.MATEMATICAS, "Números y Álgebra", Icons.Default.Percent),
                                Triple(AsignaturaUTP.LENGUAJE, "Lectura y Redacción", Icons.Default.Book),
                                Triple(AsignaturaUTP.CIENCIAS, "Experimentos", Icons.Default.Science),
                                Triple(AsignaturaUTP.HISTORIA, "Sociedad", Icons.Default.MenuBook)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    SubjectCardChoice(
                                        item = asignaturasList[0],
                                        selected = asignaturaSeleccionada == asignaturasList[0].first,
                                        onClick = { asignaturaSeleccionada = asignaturasList[0].first },
                                        modifier = Modifier.weight(1f)
                                    )
                                    SubjectCardChoice(
                                        item = asignaturasList[1],
                                        selected = asignaturaSeleccionada == asignaturasList[1].first,
                                        onClick = { asignaturaSeleccionada = asignaturasList[1].first },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    SubjectCardChoice(
                                        item = asignaturasList[2],
                                        selected = asignaturaSeleccionada == asignaturasList[2].first,
                                        onClick = { asignaturaSeleccionada = asignaturasList[2].first },
                                        modifier = Modifier.weight(1f)
                                    )
                                    SubjectCardChoice(
                                        item = asignaturasList[3],
                                        selected = asignaturaSeleccionada == asignaturasList[3].first,
                                        onClick = { asignaturaSeleccionada = asignaturasList[3].first },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                // SECCIÓN 3: TIPO DE PUBLICACIÓN
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BadgeNumber(number = "3")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tipo de Publicación *", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val tipos = TipoPublicacionEnum.values()
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    for (i in 0..2) {
                                        ResourceTypeChoice(
                                            tipo = tipos[i],
                                            selected = tipoPublicacionSeleccionado == tipos[i],
                                            onClick = { tipoPublicacionSeleccionado = tipos[i] },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    for (i in 3..5) {
                                        ResourceTypeChoice(
                                            tipo = tipos[i],
                                            selected = tipoPublicacionSeleccionado == tipos[i],
                                            onClick = { tipoPublicacionSeleccionado = tipos[i] },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // SECCIÓN DINÁMICA DINÁMICAMENTE GENERADA (PUNTO 3.1)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            when (tipoPublicacionSeleccionado) {
                                TipoPublicacionEnum.DESAFIO, TipoPublicacionEnum.PRUEBA, TipoPublicacionEnum.ACTIVIDAD -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        BadgeNumber(number = "+")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Preguntas y Desafíos (${tipoPublicacionSeleccionado.displayName})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = UtpTextPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = nuevaPreguntaText,
                                            onValueChange = { nuevaPreguntaText = it },
                                            placeholder = { Text("Escribe una pregunta o ítem...", fontSize = 12.sp) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            singleLine = true
                                        )

                                        Button(
                                            onClick = {
                                                if (nuevaPreguntaText.isNotBlank()) {
                                                    preguntasList.add(nuevaPreguntaText)
                                                    nuevaPreguntaText = ""
                                                }
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    if (preguntasList.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            preguntasList.forEachIndexed { index, preg ->
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = Color(0xFFF7F7FA),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = "${index + 1}. $preg",
                                                            fontSize = 12.sp,
                                                            color = UtpTextPrimary,
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                        IconButton(
                                                            onClick = { preguntasList.removeAt(index) },
                                                            modifier = Modifier.size(24.dp)
                                                        ) {
                                                            Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = UtpBadgeRedText, modifier = Modifier.size(16.dp))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                TipoPublicacionEnum.AUDIO -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        BadgeNumber(number = "+")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Subida de Grabación de Audio", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFF7F7FA))
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                            .clickable {
                                                audioCargadoSimulado = "grabacion_leccion_${System.currentTimeMillis() % 1000}.mp3"
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (audioCargadoSimulado == null) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Default.Mic, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(32.dp))
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("Toca para simular la grabación o subida de MP3", fontSize = 12.sp, color = Color.Gray)
                                            }
                                        } else {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = UtpBadgeGreenText, modifier = Modifier.size(24.dp))
                                                Text("Audio adjunto: $audioCargadoSimulado", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpBadgeGreenText)
                                                IconButton(onClick = { audioCargadoSimulado = null }) {
                                                    Icon(Icons.Default.Delete, contentDescription = null, tint = UtpBadgeRedText, modifier = Modifier.size(18.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                TipoPublicacionEnum.FOTO -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        BadgeNumber(number = "+")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Adjuntar Imagen / Foto", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFF7F7FA))
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                            .clickable {
                                                fotoCargadaSimulada = "imagen_recurso_${System.currentTimeMillis() % 1000}.jpg"
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (fotoCargadaSimulada == null) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(32.dp))
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("Toca para seleccionar o subir una imagen", fontSize = 12.sp, color = Color.Gray)
                                            }
                                        } else {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(Icons.Default.Image, contentDescription = null, tint = UtpBadgeBlueText, modifier = Modifier.size(24.dp))
                                                Text("Imagen lista: $fotoCargadaSimulada", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpBadgeBlueText)
                                                IconButton(onClick = { fotoCargadaSimulada = null }) {
                                                    Icon(Icons.Default.Delete, contentDescription = null, tint = UtpBadgeRedText, modifier = Modifier.size(18.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                TipoPublicacionEnum.UNIDAD -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        BadgeNumber(number = "+")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Alineación Curricular MINEDUC", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))

                                    val oas = listOf("OA1 - Demostrar comprensión", "OA2 - Lectura fluida", "OA3 - Expresión escrita")
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        oas.forEach { oa ->
                                            val isSel = oaSeleccionado == oa
                                            FilterChip(
                                                selected = isSel,
                                                onClick = { oaSeleccionado = oa },
                                                label = { Text(oa, fontSize = 10.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = UtpPurplePrimary,
                                                    selectedLabelColor = Color.White
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // SECCIÓN 4: INFORMACIÓN GENERAL
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BadgeNumber(number = "4")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Información General", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Título de la Publicación *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = tituloText,
                                onValueChange = { tituloText = it },
                                placeholder = { Text("Ej: Guía de Ejercicios N°3 o Audio de Lectura...", fontSize = 12.sp, color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Descripción / Instrucciones", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = descripcionText,
                                onValueChange = { descripcionText = it },
                                placeholder = { Text("Escribe una breve descripción o indicaciones...", fontSize = 12.sp, color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth().height(90.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun BadgeNumber(number: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFEFEFFE),
        modifier = Modifier.size(22.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(number, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UtpPurplePrimary)
        }
    }
}

@Composable
fun SubjectCardChoice(
    item: Triple<AsignaturaUTP, String, ImageVector>,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFFF0F0FF) else Color(0xFFF7F7FA),
        border = if (selected) BorderStroke(1.5.dp, UtpPurplePrimary) else null
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (selected) UtpPurplePrimary else Color.LightGray.copy(alpha = 0.3f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(item.third, contentDescription = null, tint = if (selected) Color.White else Color.DarkGray, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(item.first.displayName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = UtpTextPrimary)
                Text(item.second, fontSize = 9.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ResourceTypeChoice(
    tipo: TipoPublicacionEnum,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFFF0F0FF) else Color(0xFFF7F7FA),
        border = if (selected) BorderStroke(1.5.dp, UtpPurplePrimary) else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                tipo.icon,
                contentDescription = null,
                tint = if (selected) UtpPurplePrimary else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                tipo.displayName,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) UtpPurplePrimary else Color.Gray
            )
        }
    }
}