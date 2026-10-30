package com.example.myapplication.ui.screens.teacher

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

data class ItemContenidoAsignar(
    val id: String,
    val titulo: String,
    val tipo: String, // "ACTIVIDAD", "DESAFIO", "PRUEBA", "MULTIMEDIA"
    val descripcion: String,
    var activo: Boolean = true,
    var diasDisponible: Int = 7
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignObjectivesScreen(
    onBack: () -> Unit = {},
    onAssignedSuccess: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    // Estado independiente de asignación para cada curso
    var asignadoA by remember { mutableStateOf(true) }
    var asignadoB by remember { mutableStateOf(false) }

    // Pestaña actual de configuración (0 = 1° Año A, 1 = 1° Año B)
    var tabCursoSeleccionado by remember { mutableStateOf(0) }

    // LISTA DE CONTENIDOS INDEPENDIENTE PARA EL 1° AÑO A
    val contenidosCursoA = remember {
        mutableStateListOf(
            ItemContenidoAsignar("ca1", "Guía N°1: Trazado del 1 al 5", "ACTIVIDAD", "Ficha impresa con imágenes explicativas.", true, 7),
            ItemContenidoAsignar("ca2", "Guía N°2: Conteo de elementos", "ACTIVIDAD", "Completar cantidades y emparejar números.", true, 5),
            ItemContenidoAsignar("ca3", "Desafío: La recta numérica", "DESAFIO", "Encontrar número faltante antes del 10.", true, 3),
            ItemContenidoAsignar("ca4", "Prueba Sumativa N°1", "PRUEBA", "Evaluación escrita individual (15 ítems).", false, 10),
            ItemContenidoAsignar("ca5", "Video: Los Números Cantando", "MULTIMEDIA", "Cápsula MP4 animada de apoyo.", true, 15)
        )
    }

    // LISTA DE CONTENIDOS INDEPENDIENTE PARA EL 1° AÑO B
    val contenidosCursoB = remember {
        mutableStateListOf(
            ItemContenidoAsignar("cb1", "Guía N°1: Trazado del 1 al 5", "ACTIVIDAD", "Ficha impresa con imágenes explicativas.", true, 7),
            ItemContenidoAsignar("cb2", "Guía N°2: Conteo de elementos", "ACTIVIDAD", "Completar cantidades y emparejar números.", false, 5),
            ItemContenidoAsignar("cb3", "Desafío: La recta numérica", "DESAFIO", "Encontrar número faltante antes del 10.", false, 3),
            ItemContenidoAsignar("cb4", "Prueba Sumativa N°1", "PRUEBA", "Evaluación escrita individual (15 ítems).", true, 10),
            ItemContenidoAsignar("cb5", "Video: Los Números Cantando", "MULTIMEDIA", "Cápsula MP4 animada de apoyo.", false, 15)
        )
    }

    val contenidosActuales = if (tabCursoSeleccionado == 0) contenidosCursoA else contenidosCursoB
    val cursoNombre = if (tabCursoSeleccionado == 0) "1° Año A" else "1° Año B"
    val cursoColor = if (tabCursoSeleccionado == 0) UtpOrangeHighlight else UtpPurplePrimary
    val estaAsignadoCursoActual = if (tabCursoSeleccionado == 0) asignadoA else asignadoB

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "DOCENTE",
                showBack = true,
                onBack = onBack,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Column {
                        Text(
                            text = "Asignar Objetivo de Aprendizaje",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = UtpTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Configura de forma independiente el material y la disponibilidad para cada curso.",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                // PESTAÑAS DE NAVEGACIÓN ENTRE CURSOS (1° A vs 1° B)
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            // TAB 1° A
                            val isTabA = tabCursoSeleccionado == 0
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        if (isTabA) UtpOrangeHighlight else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { tabCursoSeleccionado = 0 },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.School,
                                        contentDescription = null,
                                        tint = if (isTabA) Color.White else UtpTextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "1° Año A",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isTabA) Color.White else UtpTextSecondary
                                    )
                                    if (asignadoA) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (isTabA) Color.White.copy(alpha = 0.3f) else UtpBadgeGreenBg
                                        ) {
                                            Text("Activo", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isTabA) Color.White else UtpBadgeGreenText, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                        }
                                    }
                                }
                            }

                            // TAB 1° B
                            val isTabB = tabCursoSeleccionado == 1
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        if (isTabB) UtpPurplePrimary else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { tabCursoSeleccionado = 1 },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.School,
                                        contentDescription = null,
                                        tint = if (isTabB) Color.White else UtpTextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "1° Año B",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isTabB) Color.White else UtpTextSecondary
                                    )
                                    if (asignadoB) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (isTabB) Color.White.copy(alpha = 0.3f) else UtpBadgeGreenBg
                                        ) {
                                            Text("Activo", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isTabB) Color.White else UtpBadgeGreenText, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // PANEL DE CONFIGURACIÓN DEL CURSO SELECCIONADO
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
                                Column {
                                    Text(
                                        text = "Habilitar Asignación para $cursoNombre",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = UtpTextPrimary
                                    )
                                    Text(
                                        text = if (estaAsignadoCursoActual) "El curso recibirá esta unidad" else "Unidad no visible para este curso",
                                        fontSize = 11.sp,
                                        color = UtpTextSecondary
                                    )
                                }

                                Switch(
                                    checked = estaAsignadoCursoActual,
                                    onCheckedChange = { isChecked ->
                                        if (tabCursoSeleccionado == 0) asignadoA = isChecked else asignadoB = isChecked
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = cursoColor
                                    )
                                )
                            }

                            if (estaAsignadoCursoActual) {
                                Spacer(modifier = Modifier.height(14.dp))
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Contenidos de $cursoNombre",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = UtpTextPrimary
                                    )
                                    val countActivos = contenidosActuales.count { it.activo }
                                    Text(
                                        text = "$countActivos de ${contenidosActuales.size} activos",
                                        fontSize = 11.sp,
                                        color = cursoColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    contenidosActuales.forEachIndexed { index, item ->
                                        val (bgBadge, textBadge) = when (item.tipo) {
                                            "ACTIVIDAD" -> Pair(UtpPurpleSoft, UtpPurplePrimary)
                                            "DESAFIO" -> Pair(UtpBadgeBlueBg, UtpBadgeBlueText)
                                            "PRUEBA" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
                                            else -> Pair(Color(0xFFD1FAE5), Color(0xFF059669))
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (item.activo) Color(0xFFF8FAFC) else Color(0xFFF1F5F9),
                                            border = if (item.activo) BorderStroke(1.dp, UtpPurpleSoft) else null,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        Surface(shape = RoundedCornerShape(6.dp), color = bgBadge) {
                                                            Text(item.tipo, color = textBadge, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                        }
                                                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE2E8F0)) {
                                                            Text("${item.diasDisponible}d disp.", fontSize = 9.sp, color = UtpTextSecondary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = item.titulo,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (item.activo) UtpTextPrimary else Color.Gray
                                                    )
                                                    Text(
                                                        text = item.descripcion,
                                                        fontSize = 10.sp,
                                                        color = UtpTextSecondary
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Switch(
                                                    checked = item.activo,
                                                    onCheckedChange = { isChecked ->
                                                        contenidosActuales[index] = item.copy(activo = isChecked)
                                                    },
                                                    colors = SwitchDefaults.colors(
                                                        checkedThumbColor = Color.White,
                                                        checkedTrackColor = cursoColor
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // BOTÓN DE CONFIRMACIÓN GLOBAL
                item {
                    val activosA = if (asignadoA) contenidosCursoA.count { it.activo } else 0
                    val activosB = if (asignadoB) contenidosCursoB.count { it.activo } else 0

                    val textoBoton = when {
                        asignadoA && asignadoB -> "Confirmar ($activosA items en 1° A • $activosB items en 1° B)"
                        asignadoA -> "Confirmar ($activosA items sólo en 1° A)"
                        asignadoB -> "Confirmar ($activosB items sólo en 1° B)"
                        else -> "Guardar como Sin Asignar"
                    }

                    Button(
                        onClick = {
                            val resumen = when {
                                asignadoA && asignadoB -> "1° A ($activosA items) y 1° B ($activosB items)"
                                asignadoA -> "sólo a 1° Año A ($activosA items)"
                                asignadoB -> "sólo a 1° Año B ($activosB items)"
                                else -> "ningún curso (Sin Asignar)"
                            }
                            Toast.makeText(context, "Unidad asignada exitosamente a $resumen.", Toast.LENGTH_LONG).show()
                            onAssignedSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpPurplePrimary)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = textoBoton,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}