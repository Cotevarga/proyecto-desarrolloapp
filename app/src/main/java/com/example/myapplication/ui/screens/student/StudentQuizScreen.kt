package com.example.myapplication.ui.screens.student

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun StudentQuizScreen(
    onBack: () -> Unit = {},
    onFinishQuiz: () -> Unit = {}
) {
    val context = LocalContext.current

    var seleccionOpcion by remember { mutableStateOf<Int?>(null) }
    var pruebaEnviada by remember { mutableStateOf(false) }
    var intentoActual by remember { mutableStateOf(1) }

    val maxIntentosDocente = 2
    val respuestaCorrectaId = 2

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = UtpTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Prueba Sumativa OA1",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                    }

                    IconButton(
                        onClick = {
                            Toast.makeText(context, "🔊 Pregunta: ¿Cuántas manzanas hay en la imagen?", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar Pregunta", tint = UtpRed)
                    }
                }
            }

            // AVISO EN ROJO DE INTEGRACIÓN DE BD
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
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
                            modifier = Modifier.size(20.dp)
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

            item {
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
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = UtpRed.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "Pregunta 1 de 1",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UtpRed,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = "Intento $intentoActual de $maxIntentosDocente",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = UtpTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "¿Cuántas manzanas hay en la imagen?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🍎 🍎 🍎 🍎 🍎", fontSize = 32.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val opciones = listOf(
                            Pair(1, "3 Manzanas"),
                            Pair(2, "5 Manzanas"),
                            Pair(3, "7 Manzanas")
                        )

                        opciones.forEach { (opcionId, texto) ->
                            val esEstaSeleccionada = seleccionOpcion == opcionId
                            var fondoColor = Color.White
                            var bordeColor = UtpRed.copy(alpha = 0.3f)

                            if (pruebaEnviada) {
                                if (opcionId == respuestaCorrectaId) {
                                    fondoColor = Color(0xFFD1FAE5)
                                    bordeColor = Color(0xFF059669)
                                } else if (esEstaSeleccionada && opcionId != respuestaCorrectaId) {
                                    fondoColor = Color(0xFFFEE2E2)
                                    bordeColor = Color(0xFFDC2626)
                                }
                            } else if (esEstaSeleccionada) {
                                fondoColor = UtpRed.copy(alpha = 0.1f)
                                bordeColor = UtpRed
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = fondoColor,
                                border = BorderStroke(1.5.dp, bordeColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = !pruebaEnviada) {
                                        seleccionOpcion = opcionId
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = texto,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = UtpTextPrimary
                                    )

                                    if (pruebaEnviada) {
                                        if (opcionId == respuestaCorrectaId) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Correcta", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                                            }
                                        } else if (esEstaSeleccionada && opcionId != respuestaCorrectaId) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Incorrecta", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(visible = pruebaEnviada) {
                    val esCorrecto = seleccionOpcion == respuestaCorrectaId
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = if (esCorrecto) Color(0xFFD1FAE5) else Color(0xFFFEE2E2))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (esCorrecto) "🎉 ¡Excelente Trabajo!" else "❌ Revisa tu Respuesta",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (esCorrecto) Color(0xFF059669) else Color(0xFFDC2626)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (esCorrecto) "Respondiste correctamente la prueba de conteo." else "Puedes volver a intentar si aún tienes intentos disponibles.",
                                fontSize = 11.sp,
                                color = UtpTextPrimary
                            )
                        }
                    }
                }
            }

            item {
                if (!pruebaEnviada) {
                    Button(
                        onClick = {
                            if (seleccionOpcion == null) {
                                Toast.makeText(context, "Por favor selecciona una respuesta", Toast.LENGTH_SHORT).show()
                            } else {
                                pruebaEnviada = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtpRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Comprobar Respuesta", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (intentoActual < maxIntentosDocente) {
                            OutlinedButton(
                                onClick = {
                                    pruebaEnviada = false
                                    seleccionOpcion = null
                                    intentoActual++
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = UtpRed),
                                border = BorderStroke(1.dp, UtpRed)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Repetir Prueba", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = onFinishQuiz,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UtpRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Finalizar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(60.dp)) }
        }
    }
}