package com.example.myapplication.ui.screens.teacher

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.SolicitudPermiso
import com.example.myapplication.data.repository.TeacherRepository
import com.example.myapplication.ui.theme.*

enum class TipoSolicitudEnum(val displayName: String, val icon: ImageVector) {
    PRUEBA("Prueba", Icons.Default.Edit),
    UNIDAD("Unidad", Icons.Default.Book),
    DESAFIO("Desafío", Icons.Default.EmojiEvents),
    FOTO("Foto", Icons.Default.CameraAlt),
    AUDIO("Audio", Icons.Default.Mic),
    ACTIVIDAD("Actividad", Icons.Default.Assignment)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherRequestScreen(
    onBack: () -> Unit = {},
    repository: TeacherRepository = remember { TeacherRepository() }
) {
    val context = LocalContext.current

    var cursoSeleccionado by remember { mutableStateOf("1° Año A") }
    var tipoSolicitudSeleccionado by remember { mutableStateOf(TipoSolicitudEnum.UNIDAD) }

    var tituloText by remember { mutableStateOf("") }
    var descripcionText by remember { mutableStateOf("") }

    fun limpiarCampos() {
        tituloText = ""
        descripcionText = ""
        cursoSeleccionado = "1° Año A"
        tipoSolicitudSeleccionado = TipoSolicitudEnum.UNIDAD
    }

    fun enviarSolicitud() {
        if (tituloText.isNotBlank()) {
            val nuevaSolicitud = SolicitudPermiso(
                id = "sol_${System.currentTimeMillis()}",
                titulo = "$tituloText ($cursoSeleccionado)",
                tipoPermiso = tipoSolicitudSeleccionado.displayName,
                fechaSolicitud = "30 Oct 2026",
                estado = "PENDIENTE",
                motivoDocente = descripcionText.ifBlank { "Solicitud para $cursoSeleccionado de tipo ${tipoSolicitudSeleccionado.displayName}." },
                respuestaUtp = "Solicitud ingresada. En revisión por Jefatura UTP."
            )

            repository.agregarSolicitud(nuevaSolicitud)
            Toast.makeText(context, "Solicitud enviada a Jefatura UTP", Toast.LENGTH_SHORT).show()
            onBack()
        } else {
            Toast.makeText(context, "Por favor ingresa un título para la solicitud", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // CABECERA SUPERIOR IDÉNTICA A JEFATURA
            Surface(color = Color.White, shadowElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { limpiarCampos() }) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Limpiar", color = Color.Gray, fontSize = 14.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Nueva Solicitud", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = UtpTextPrimary)
                        Text("Solicitud a UTP", fontSize = 11.sp, color = UtpPurplePrimary)
                    }

                    Button(
                        onClick = { enviarSolicitud() },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Solicitar", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
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

                // SECCIÓN 2: TIPO DE SOLICITUD (Sustituye la de Asignatura que se eliminó)
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
                                Text("Tipo de Solicitud *", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val tipos = TipoSolicitudEnum.values()
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    for (i in 0..2) {
                                        ResourceTypeChoiceTeacher(
                                            tipo = tipos[i],
                                            selected = tipoSolicitudSeleccionado == tipos[i],
                                            onClick = { tipoSolicitudSeleccionado = tipos[i] },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    for (i in 3..5) {
                                        ResourceTypeChoiceTeacher(
                                            tipo = tipos[i],
                                            selected = tipoSolicitudSeleccionado == tipos[i],
                                            onClick = { tipoSolicitudSeleccionado = tipos[i] },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // SECCIÓN 3: INFORMACIÓN GENERAL
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
                                Text("Información General", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UtpTextPrimary)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Título de la Solicitud *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = tituloText,
                                onValueChange = { tituloText = it },
                                placeholder = { Text("Ej: Modificación Unidad 1 o Evaluación Diferida...", fontSize = 12.sp, color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Descripción / Motivo Pedagógico", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = descripcionText,
                                onValueChange = { descripcionText = it },
                                placeholder = { Text("Escribe el motivo o detalle pedagógico de tu solicitud...", fontSize = 12.sp, color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth().height(90.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun BadgeNumber(number: String) {
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
private fun ResourceTypeChoiceTeacher(
    tipo: TipoSolicitudEnum,
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