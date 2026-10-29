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
import com.example.myapplication.ui.theme.UtpRed

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
                1, "100000I68N", "Desarrollo de Apps Móviles", 17.5,
                listOf(
                    GradeDetail("Evaluación Continua 1 (EC1)", "20%", "18.0"),
                    GradeDetail("Examen Parcial (EP)", "25%", "16.5"),
                    GradeDetail("Evaluación Continua 2 (EC2)", "25%", "18.0"),
                    GradeDetail("Examen Final (EF)", "30%", "Pendiente")
                )
            ),
            CourseGrades(
                2, "100000I72N", "Base de Datos Avanzada", 16.0,
                listOf(
                    GradeDetail("Evaluación Continua 1 (EC1)", "20%", "15.0"),
                    GradeDetail("Examen Parcial (EP)", "25%", "17.0"),
                    GradeDetail("Evaluación Continua 2 (EC2)", "25%", "16.0"),
                    GradeDetail("Examen Final (EF)", "30%", "Pendiente")
                )
            ),
            CourseGrades(
                3, "100000I81N", "Ingeniería de Software II", 15.2,
                listOf(
                    GradeDetail("Evaluación Continua 1 (EC1)", "20%", "14.0"),
                    GradeDetail("Examen Parcial (EP)", "25%", "16.0"),
                    GradeDetail("Evaluación Continua 2 (EC2)", "25%", "15.5"),
                    GradeDetail("Examen Final (EF)", "30%", "Pendiente")
                )
            ),
            CourseGrades(
                4, "100000I55N", "Arquitectura de Computadores", 14.8,
                listOf(
                    GradeDetail("Evaluación Continua 1 (EC1)", "20%", "13.5"),
                    GradeDetail("Examen Parcial (EP)", "25%", "15.0"),
                    GradeDetail("Evaluación Continua 2 (EC2)", "25%", "15.5"),
                    GradeDetail("Examen Final (EF)", "30%", "Pendiente")
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
                text = "Mis Cursos y Notas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = "Periodo Académico 2026-1",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 48.dp, bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses) { course ->
                CourseGradeCard(course = course)
            }
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = course.courseName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (course.averageGrade >= 12.0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = "Prom: ${course.averageGrade}",
                        color = if (course.averageGrade >= 12.0) Color(0xFF2E7D32) else UtpRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Detalle de Calificaciones:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    course.details.forEach { detail ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${detail.evaluationName} (${detail.weight})",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = detail.grade,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (detail.grade == "Pendiente") Color.Gray else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}