package com.example.myapplication.ui.screens.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

data class GradeDetail(
    val evaluationName: String,
    val weight: String,
    val grade: String
)

data class CourseGrades(
    val id: Int,
    val courseCode: String,
    val courseName: String,
    val averageGrade: Double,
    val details: List<GradeDetail>
)

@Composable
fun GradesScreen(
    onBack: () -> Unit = {}
) {
    val courses = remember {
        listOf(
            CourseGrades(
                1, "MAT-1B", "Matemática", 6.8,
                listOf(
                    GradeDetail("Guía N°1: Trazado del 1 al 5", "20%", "7.0"),
                    GradeDetail("Guía N°2: Conteo de Elementos", "30%", "6.5"),
                    GradeDetail("Desafío N°1: La Recta Numérica", "20%", "7.0"),
                    GradeDetail("Evaluación Sumativa OA1", "30%", "Pendiente")
                )
            ),
            CourseGrades(
                2, "LEN-1B", "Lenguaje y Comunicación", 6.2,
                listOf(
                    GradeDetail("Lectura de Vocales", "25%", "6.0"),
                    GradeDetail("Escritura de Sílabas Iniciales", "25%", "6.5"),
                    GradeDetail("Comprensión Lectora Guiada", "50%", "Pendiente")
                )
            ),
            CourseGrades(
                3, "NAT-1B", "Ciencias Naturales", 6.5,
                listOf(
                    GradeDetail("Actividad: Los Cinco Sentidos", "40%", "6.8"),
                    GradeDetail("Ficha: Animales y su Hábitat", "60%", "6.2")
                )
            ),
            CourseGrades(
                4, "HIS-1B", "Historia, Geografía y C. Soc.", 7.0,
                listOf(
                    GradeDetail("Dibujo: Mi Familia y Comunidad", "50%", "7.0"),
                    GradeDetail("Ubicación en el Tiempo (Días)", "50%", "7.0")
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mis Notas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary
            )
        }

        Text(
            text = "Año Académico 2026 • 1° Año A",
            fontSize = 12.sp,
            color = UtpTextSecondary,
            modifier = Modifier.padding(start = 48.dp, bottom = 14.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses) { course ->
                CourseGradeCard(course = course)
            }
            item { Spacer(modifier = Modifier.height(60.dp)) }
        }
    }
}

@Composable
fun CourseGradeCard(course: CourseGrades) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.courseCode,
                        fontSize = 11.sp,
                        color = UtpTextSecondary
                    )
                    Text(
                        text = course.courseName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = UtpTextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (course.averageGrade >= 4.0) UtpBadgeGreenBg else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = "Prom: ${course.averageGrade}",
                        color = if (course.averageGrade >= 4.0) UtpBadgeGreenText else Color(0xFFDC2626),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = UtpPurplePrimary
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Detalle de Evaluaciones:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = UtpTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    course.details.forEach { detail ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${detail.evaluationName} (${detail.weight})",
                                fontSize = 12.sp,
                                color = UtpTextSecondary
                            )
                            Text(
                                text = detail.grade,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (detail.grade == "Pendiente") Color(0xFFD97706) else UtpTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}