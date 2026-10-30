package com.example.myapplication.ui.screens.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.myapplication.data.model.CourseGrades
import com.example.myapplication.data.repository.StudentRepository
import com.example.myapplication.ui.components.UtpTopHeaderBar
import com.example.myapplication.ui.theme.*

@Composable
fun GradesScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    repository: StudentRepository = remember { StudentRepository() }
) {
    val courses = remember { repository.getNotasAlumno() }

    Box(modifier = Modifier.fillMaxSize().background(UtpWarmBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UtpTopHeaderBar(
                userRole = "ALUMNO",
                userName = "Camila Morales (1° A)",
                showBack = false,
                onLogout = onLogout
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    Column {
                        Text(
                            text = "Mis Notas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UtpTextPrimary
                        )
                        Text(
                            text = "Año Académico 2026 • 1° Año A",
                            fontSize = 12.sp,
                            color = UtpTextSecondary
                        )
                    }
                }

                items(courses) { course ->
                    CourseGradeCard(course = course)
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
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
                        tint = UtpRed
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