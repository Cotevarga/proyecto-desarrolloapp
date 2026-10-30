package com.example.myapplication.data.repository

import com.example.myapplication.data.model.DocenteModel
import com.example.myapplication.data.model.EstudianteAvance
import com.example.myapplication.data.model.SolicitudPermiso

class TeacherRepository {

    private val solicitudesMutables = mutableListOf(
        SolicitudPermiso(
            id = "sol_1",
            titulo = "Modificación de secuencia OA1 Matemática",
            tipoPermiso = "Cambio de Planificación",
            fechaSolicitud = "14 Oct 2026",
            estado = "ACEPTADO",
            motivoDocente = "Se requiere incluir 1 clase extra de nivelación antes de la prueba final.",
            respuestaUtp = "Aprobado por Jefatura UTP. Se actualizó el calendario escolar del curso."
        ),
        SolicitudPermiso(
            id = "sol_2",
            titulo = "Aplicación de Evaluación Diferida (Lucas Morales)",
            tipoPermiso = "Evaluación Diferida",
            fechaSolicitud = "22 Oct 2026",
            estado = "PENDIENTE",
            motivoDocente = "Estudiante presentó licencia médica durante la prueba sumativa.",
            respuestaUtp = "En revisión por la Coordinadora PIE y UTP."
        ),
        SolicitudPermiso(
            id = "sol_3",
            titulo = "Salida Pedagógica a Museo de Ciencias",
            tipoPermiso = "Permiso Administrativo",
            fechaSolicitud = "28 Oct 2026",
            estado = "RECHAZADO",
            motivoDocente = "Actividad complementaria para la unidad de Seres Vivos.",
            respuestaUtp = "Rechazado debido a tope de fechas con la semana de evaluaciones globales."
        )
    )

    fun getDocentes(): List<DocenteModel> {
        return listOf(
            DocenteModel("doc_1", "Prof. María Angélica", "Matemática", listOf("Matemática"), listOf("1° A", "1° B")),
            DocenteModel("doc_2", "Prof. Roberto Gómez", "Lenguaje y Comunicación", listOf("Lenguaje y Comunicación"), listOf("1° A")),
            DocenteModel("doc_3", "Prof. Carolina Henríquez", "Ciencias Naturales", listOf("Ciencias Naturales", "Historia"), listOf("1° B"))
        )
    }

    fun getDocenteById(id: String): DocenteModel {
        return getDocentes().firstOrNull { it.id == id } ?: getDocentes().first()
    }

    fun getEstudiantesPorDocente(docenteId: String): List<EstudianteAvance> {
        val docente = getDocenteById(docenteId)
        val todos = listOf(
            EstudianteAvance("1", "Camila Morales", "24.123.456-1", "1° A", "3/3 Completados", "Finalizado", "4/5 Correctas (80%)", emptyList()),
            EstudianteAvance("2", "Carlos Silva", "24.234.567-2", "1° A", "1/3 Completados", "Pendiente", "Sin rendir", emptyList()),
            EstudianteAvance("3", "Carolina Fuentes", "24.345.678-3", "1° B", "3/3 Completados", "Finalizado", "5/5 Correctas (100%)", emptyList())
        )
        return todos.filter { docente.seccionesAsignadas.contains(it.curso) }
    }

    fun getSolicitudesPermiso(): List<SolicitudPermiso> {
        return solicitudesMutables.toList()
    }

    fun agregarSolicitud(solicitud: SolicitudPermiso) {
        solicitudesMutables.add(0, solicitud)
    }
}