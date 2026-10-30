package com.example.myapplication.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class EstudianteAvance(
    val id: String = "1",
    val nombre: String,
    val curso: String,
    val oa: String = "OA1 - Matemática",
    val avance: String = "100%",
    val estado: String = "En Progreso",
    val rut: String = "",
    val desafiosCompletados: String = "3/3",
    val estadoDesafios: String = "Completado",
    val pruebaPuntaje: String = "7.0",
    val preguntasDetalle: List<Pair<String, Boolean>> = emptyList()
) {
    val progreso: String get() = avance
}

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

data class ClassSchedule(
    val id: Int,
    val courseName: String,
    val time: String,
    val classroom: String,
    val professor: String,
    val modality: String,
    val day: String
)

data class AsignaturaAlumno(
    val id: String,
    val nombre: String,
    val color: Color,
    val icon: ImageVector,
    val objetivosAsignadosCount: Int
)