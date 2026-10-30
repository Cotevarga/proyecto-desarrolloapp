package com.example.myapplication.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DocenteModel(
    val id: String,
    val nombre: String,
    val materiaPrincipal: String,
    val asignaturas: List<String>,
    val seccionesAsignadas: List<String>
)

data class ContenidoBiblioteca(
    val id: String,
    val asignatura: String,
    val unidadCodigo: String,       // Ej: "Unidad 1", "Unidad 2"
    val titulo: String,
    val descripcion: String,
    val actividadesConteo: Int,
    val desafiosConteo: Int,
    val pruebaConteo: Int,
    val multimediaConteo: Int,
    val icon: ImageVector
)

data class SolicitudPermiso(
    val id: String,
    val titulo: String,
    val tipoPermiso: String,
    val fechaSolicitud: String,
    val estado: String,
    val motivoDocente: String,
    val respuestaUtp: String? = null
)