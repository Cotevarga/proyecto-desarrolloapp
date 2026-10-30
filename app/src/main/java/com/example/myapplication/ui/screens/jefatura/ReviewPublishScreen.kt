package com.example.myapplication.ui.screens.jefatura

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

data class UnidadPublicacion(
    val id: String,
    val asignatura: String,
    val unidadCodigo: String,
    val titulo: String,
    val descripcion: String,
    val estado: String, // "PENDIENTE", "PUBLICADO", "RECHAZADO"
    val fechaSolicitud: String,
    val docente: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPublishScreen(
    onBack: () -> Unit = {},
    onEditUnit: () -> Unit = {},
    onAccountSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var unidadSeleccionadaModal by remember { mutableStateOf<UnidadPublicacion?>(null) }

    val listaUnidades = remember {
        mutableStateListOf(
            UnidadPublicacion(
                id = "u1",
                asignatura = "Matemática",
                unidadCodigo = "Unidad 1",
                titulo = "Contamos y representamos del 1 al 10",
                descripcion = "Contenidos de conteo, guías de trazado y evaluación sumativa inicial.",
                estado = "PENDIENTE",
                fechaSolicitud = "28 Oct 2026",
                docente = "Prof. María Angélica"
            ),
            UnidadPublicacion(
                id = "u2",
                asignatura = "Lenguaje y Comunicación",
                unidadCodigo = "Unidad 2",
                titulo = "Lectura y escritura de vocales",
                descripcion = "Planificación para 1° Básico A y B con recursos interactivos.",
                estado = "PUBLICADO",
                fechaSolicitud = "20 Oct 2026",
                docente = "Prof. Roberto Gómez"
            )
        )
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    Column {
                        Text(
                            text = "Revisión y Publicación de Unidades",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Text(
                            text = "Aprueba las propuestas pedagógicas preparadas por el cuerpo docente",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                items(listaUnidades) { unidad ->
                    val isPending = unidad.estado == "PENDIENTE"
                    val (badgeBg, badgeText) = if (isPending) {
                        Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
                    } else {
                        Pair(UtpBadgeGreenBg, UtpBadgeGreenText)
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
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = UtpPurpleSoft
                                    ) {
                                        Text(
                                            text = unidad.asignatura,
                                            color = UtpPurplePrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = unidad.unidadCodigo,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpTextSecondary
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = badgeBg
                                ) {
                                    Text(
                                        text = unidad.estado,
                                        color = badgeText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(unidad.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(unidad.descripcion, fontSize = 12.sp, color = UtpTextSecondary)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Enviado por: ${unidad.docente} (${unidad.fechaSolicitud})", fontSize = 11.sp, color = UtpTextSecondary)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { unidadSeleccionadaModal = unidad },
                                    colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Detalles de Unidad", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        // --- VENTANA EMERGENTE DE DETALLES ---
        unidadSeleccionadaModal?.let { unidad ->
            AlertDialog(
                onDismissRequest = { unidadSeleccionadaModal = null },
                title = {
                    Column {
                        Text(
                            text = "${unidad.unidadCodigo}: ${unidad.titulo}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${unidad.asignatura} • ${unidad.docente}",
                            fontSize = 12.sp,
                            color = Color(0xFFC7D2FE)
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Descripción Pedagógica:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = unidad.descripcion,
                            fontSize = 12.sp,
                            color = Color.White
                        )

                        // AVISO SOLICITADO
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
                                    text = "En proceso de integración de base de datos",
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
                            unidadSeleccionadaModal = null
                            onEditUnit()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Editar Unidad", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { unidadSeleccionadaModal = null }) {
                        Text("Cerrar", fontSize = 12.sp, color = Color.LightGray)
                    }
                }
            )
        }
    }
}