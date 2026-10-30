package com.example.myapplication.ui.screens.student

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

data class AsignaturaAlumno(
    val id: String,
    val nombre: String,
    val color: Color,
    val icon: ImageVector,
    val objetivosAsignadosCount: Int
)

@Composable
fun StudentHomeScreen(
    onSelectAsignatura: (String) -> Unit = {}
) {
    val asignaturas = remember {
        listOf(
            AsignaturaAlumno("mat", "Matemática", Color(0xFF6366F1), Icons.Default.Calculate, 1),
            AsignaturaAlumno("len", "Lenguaje", Color(0xFFEC4899), Icons.Default.MenuBook, 1),
            AsignaturaAlumno("cie", "Ciencias Naturales", Color(0xFF10B981), Icons.Default.Park, 1),
            AsignaturaAlumno("his", "Historia y Entorno", Color(0xFFF59E0B), Icons.Default.Public, 1)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = UtpPurplePrimary)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "¡Hola, Cami! 👋",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "¡Sigue aprendiendo y gana estrellas!",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFACC15), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("8/10", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Mi Avance General", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { 0.8f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF22C55E),
                        trackColor = Color.White.copy(alpha = 0.3f),
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
            }
        }

        item {
            Text(
                text = "Mis 4 Asignaturas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                asignaturas.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { asig ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSelectAsignatura(asig.id) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = asig.color.copy(alpha = 0.15f),
                                        modifier = Modifier.size(42.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(asig.icon, contentDescription = null, tint = asig.color, modifier = Modifier.size(24.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(asig.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
                                    Text("${asig.objetivosAsignadosCount} Objetivo Asignado", fontSize = 10.sp, color = UtpTextSecondary)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = asig.color
                                    ) {
                                        Text(
                                            text = "Ver Unidad",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}