package com.example.myapplication.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.model.*
import com.example.myapplication.ui.theme.UtpRed

class StudentRepository {

    private val asignaturas = listOf(
        AsignaturaAlumno("mat", "Matemática", UtpRed, Icons.Default.Calculate, 1),
        AsignaturaAlumno("len", "Lenguaje", Color(0xFFEC4899), Icons.Default.MenuBook, 1),
        AsignaturaAlumno("cie", "Ciencias Naturales", Color(0xFF10B981), Icons.Default.Park, 1),
        AsignaturaAlumno("his", "Historia y Entorno", Color(0xFFF59E0B), Icons.Default.Public, 1)
    )

    private val notasEstudiante = mutableListOf(
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

    private val horarioClases = listOf(
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

    fun getAsignaturas(): List<AsignaturaAlumno> = asignaturas

    fun getNotasAlumno(): List<CourseGrades> = notasEstudiante.toList()

    fun getHorarioPorDia(dia: String): List<ClassSchedule> {
        return horarioClases.filter { it.day.equals(dia, ignoreCase = true) }
    }

    fun registrarResultadoPrueba(courseCode: String, evaluacionName: String, nota: String) {
        val cursoIndex = notasEstudiante.indexOfFirst { it.courseCode == courseCode }
        if (cursoIndex != -1) {
            val curso = notasEstudiante[cursoIndex]
            val nuevosDetalles = curso.details.map {
                if (it.evaluationName == evaluacionName) it.copy(grade = nota) else it
            }
            notasEstudiante[cursoIndex] = curso.copy(details = nuevosDetalles)
        }
    }
}