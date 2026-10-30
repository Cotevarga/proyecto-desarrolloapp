package com.example.myapplication.ui.screens.student

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
import com.example.myapplication.ui.theme.*

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
    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    var selectedDay by remember { mutableStateOf("Lunes") }

    val schedules = remember {
        listOf(
            ClassSchedule(1, "Matemática", "08:30 - 10:00", "Sala 1° A", "Prof. María Angélica", "Presencial", "Lunes"),
            ClassSchedule(2, "Lenguaje y Comunicación", "10:15 - 11:45", "Sala 1° A", "Prof. Roberto Gómez", "Presencial", "Lunes"),
            ClassSchedule(3, "Lenguaje y Comunicación", "08:30 - 10:00", "Sala 1° A", "Prof. Roberto Gómez", "Presencial", "Martes"),
            ClassSchedule(4, "Ciencias Naturales", "10:15 - 11:45", "Sala 1° A", "Prof. Carolina Henríquez", "Presencial", "Martes"),
            ClassSchedule(5, "Matemática", "08:30 - 10:00", "Sala 1° A", "Prof. María Angélica", "Presencial", "Miércoles"),
            ClassSchedule(6, "Historia y Geografía", "10:15 - 11:45", "Sala 1° A", "Prof. Carolina Henríquez", "Presencial", "Miércoles"),
            ClassSchedule(7, "Educación Física", "08:30 - 10:00", "Patio Central", "Prof. Felipe Soto", "Presencial", "Jueves"),
            ClassSchedule(8, "Artes Visuales", "10:15 - 11:45", "Taller de Artes", "Prof. Ana María", "Presencial", "Jueves"),
            ClassSchedule(9, "Matemática", "08:30 - 10:00", "Sala 1° A", "Prof. María Angélica", "Presencial", "Viernes"),
            ClassSchedule(10, "Música", "10:15 - 11:45", "Sala de Música", "Prof. Jorge Luis", "Presencial", "Viernes")
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = UtpTextPrimary
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(days) { day ->
                val isSelected = day == selectedDay
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDay = day },
                    label = { Text(day, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = UtpPurplePrimary,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredSchedules.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay clases registradas para este día",
                    color = UtpTextSecondary,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredSchedules) { schedule ->
                    ScheduleCard(schedule = schedule)
                }
                item { Spacer(modifier = Modifier.height(60.dp)) }
            }
        }
    }
}

@Composable
fun ScheduleCard(schedule: ClassSchedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = UtpCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = schedule.courseName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = UtpTextPrimary
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = UtpBadgeGreenBg
                ) {
                    Text(
                        text = schedule.modality,
                        color = UtpBadgeGreenText,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = UtpPurplePrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = schedule.time, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UtpTextPrimary)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Room, contentDescription = null, tint = UtpTextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = schedule.classroom, fontSize = 12.sp, color = UtpTextSecondary)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = UtpTextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = schedule.professor, fontSize = 12.sp, color = UtpTextSecondary)
            }
        }
    }
}