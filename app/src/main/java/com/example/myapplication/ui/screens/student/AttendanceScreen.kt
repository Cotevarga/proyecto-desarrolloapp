package com.example.myapplication.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.UtpRed

data class CourseAttendance(
    val courseName: String,
    val totalSessions: Int,
    val attendedSessions: Int,
    val missedSessions: Int,
    val maxAllowedAbsences: Int
) {
    val percentage: Float
        get() = if (totalSessions > 0) (attendedSessions.toFloat() / totalSessions.toFloat()) else 1f
}

@Composable
fun AttendanceScreen() {
    val attendanceList = remember {
        listOf(
            CourseAttendance("Desarrollo de Apps Móviles", 16, 15, 1, 4),
            CourseAttendance("Base de Datos Avanzada", 16, 14, 2, 4),
            CourseAttendance("Ingeniería de Software II", 16, 16, 0, 4),
            CourseAttendance("Arquitectura de Computadores", 16, 13, 3, 4)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Control de Asistencia",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Límite máximo permitido de inasistencias: 30%",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(attendanceList) { item ->
                AttendanceCard(attendance = item)
            }
        }
    }
}

@Composable
fun AttendanceCard(attendance: CourseAttendance) {
    val percentInt = (attendance.percentage * 100).toInt()
    val isWarning = attendance.missedSessions >= (attendance.maxAllowedAbsences - 1)

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                Text(
                    text = attendance.courseName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$percentInt%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isWarning) UtpRed else Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { attendance.percentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (isWarning) UtpRed else Color(0xFF2E7D32),
                trackColor = Color.LightGray.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Asistidas: ${attendance.attendedSessions}/${attendance.totalSessions}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Inasistencias: ${attendance.missedSessions} (Máx: ${attendance.maxAllowedAbsences})",
                    fontSize = 13.sp,
                    color = if (isWarning) UtpRed else Color.Gray,
                    fontWeight = if (isWarning) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
