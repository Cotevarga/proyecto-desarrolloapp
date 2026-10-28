package com.example.myapplication.ui.model

// Estados permitidos para el flujo de validación UTP
enum class EstadoContenido {
    BORRADOR,
    PENDIENTE,
    APROBADO,
    RECHAZADO,
    PUBLICADO,
    DESHABILITADO
}

// Las 4 asignaturas obligatorias para 1° Básico
enum class AsignaturaUTP(val displayName: String) {
    LENGUAJE("Lenguaje"),
    MATEMATICAS("Matemáticas"),
    CIENCIAS("Ciencias Naturales"),
    HISTORIA("Historia")
}

// Estructura de Unidades Pedagógicas
data class UnidadPedagogica(
    val id: String,
    val asignatura: AsignaturaUTP,
    val numero: Int,
    val titulo: String,
    val descripcion: String,
    val estado: EstadoContenido = EstadoContenido.PENDIENTE,
    val observacionesUTP: String? = null,
    val modificadoPor: String = "Docente 1° Básico",
    val timestamp: Long = System.currentTimeMillis()
)

// Estructura de Objetivos de Aprendizaje (OA) con recursos multimedia
data class ObjetivoAprendizaje(
    val id: String,
    val unidadId: String,
    val codigoOA: String, // Ej: "OA 01"
    val explicacionEscrita: String,
    val audioUrl: String? = null,   // Audio preparado/autorizado
    val imagenUrl: String? = null   // Imagen propia, pública o autorizada
)

// Actividades y Desafíos
data class ActividadDesafio(
    val id: String,
    val oaId: String,
    val titulo: String,
    val instrucciones: String,
    val esDesafio: Boolean = false // true = Desafío del OA, false = Actividad regular
)

// Preguntas y Evaluación Final
data class PreguntaPrueba(
    val id: String,
    val oaId: String, // Relación directa con el Objetivo de Aprendizaje
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrectaIndex: Int
)

data class PruebaFinal(
    val id: String,
    val unidadId: String,
    val titulo: String,
    val preguntas: List<PreguntaPrueba>,
    val estado: EstadoContenido = EstadoContenido.BORRADOR
)

// Registro de Auditoría e Historial de Cambios
data class HistorialAuditoria(
    val id: String,
    val entidadId: String,
    val accion: String, // APROBAR, RECHAZAR, PUBLICAR, DESHABILITAR, CREAR, EDITAR
    val usuario: String = "Jefatura UTP",
    val detalle: String,
    val timestamp: Long = System.currentTimeMillis()
)