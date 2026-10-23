package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.UtpRed

data class ClassSchedule(
    val id: Int,
    val courseName: String,
    val time: String,
    val classroom: String,
    val professor: String,
    val modality: String,
    val day: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBack: () -> Unit = {}
) {
    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
    var selectedDay by remember { mutableStateOf("Lunes") }

    val schedules = remember {
        listOf(
            ClassSchedule(1, "Desarrollo de Apps Móviles", "18:30 - 21:30", "Aula A-402 • Pabellón A", "Ing. Carlos Mendoza", "Presencial", "Lunes"),
            ClassSchedule(2, "Base de Datos Avanzada", "19:15 - 22:15", "Laboratorio L-201", "Dra. Ana Torres", "Presencial", "Martes"),
            ClassSchedule(3, "Ingeniería de Software II", "18:30 - 20:45", "Virtual (Canvas UTP)", "Mg. Roberto Gomez", "Virtual", "Miércoles"),
            ClassSchedule(4, "Arquitectura de Computadores", "20:00 - 22:15", "Aula B-305", "Ing. Luis Vargas", "Presencial", "Jueves"),
            ClassSchedule(5, "Ética Profesional", "18:30 - 20:00", "Virtual (Canvas UTP)", "Lic. Elena Rios", "Virtual", "Viernes"),
            ClassSchedule(6, "Taller de Proyectos", "08:00 - 12:00", "Laboratorio L-105", "Ing. Miguel Ramos", "Presencial", "Sábado")
        )
    }

    val filteredSchedules = schedules.filter { it.day.equals(selectedDay, ignoreCase = true) }

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
                text = "Horario de Clases",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(days) { day ->
                val isSelected = day == selectedDay
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDay = day },
                    label = { Text(day, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = UtpRed,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredSchedules.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes clases programadas para este día",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredSchedules) { schedule ->
                    ScheduleCard(schedule = schedule)
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(schedule: ClassSchedule) {
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
                    text = schedule.courseName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                val isVirtual = schedule.modality.equals("Virtual", ignoreCase = true)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isVirtual) Color(0xFFE3F2FD) else Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = schedule.modality,
                        color = if (isVirtual) Color(0xFF1565C0) else Color(0xFF2E7D32),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = UtpRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = schedule.time, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Room, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = schedule.classroom, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = schedule.professor, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}