package com.example.myapplication.ui.repository

import com.example.myapplication.ui.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object UtpRepository {

    // --- BASE DE DATOS EN MEMORIA (1° BÁSICO) ---
    private val unidadesLista = mutableListOf(
        UnidadPedagogica(
            id = "u1",
            asignatura = AsignaturaUTP.LENGUAJE,
            numero = 1,
            titulo = "Lectura inicial de vocales",
            descripcion = "Comprensión lectora y reconocimiento fonológico para 1° Básico.",
            estado = EstadoContenido.PENDIENTE
        ),
        UnidadPedagogica(
            id = "u2",
            asignatura = AsignaturaUTP.MATEMATICAS,
            numero = 1,
            titulo = "Conteo y números del 0 al 20",
            descripcion = "Representación de cantidades y razonamiento matemático inicial.",
            estado = EstadoContenido.APROBADO
        ),
        UnidadPedagogica(
            id = "u3",
            asignatura = AsignaturaUTP.CIENCIAS,
            numero = 1,
            titulo = "Los sentidos y el cuerpo humano",
            descripcion = "Identificación de funciones básicas del cuerpo humano.",
            estado = EstadoContenido.PENDIENTE
        ),
        UnidadPedagogica(
            id = "u4",
            asignatura = AsignaturaUTP.HISTORIA,
            numero = 1,
            titulo = "Secuencia temporal y la familia",
            descripcion = "Nociones de tiempo, días de la semana y entorno cercano.",
            estado = EstadoContenido.BORRADOR
        )
    )

    private val oasLista = mutableListOf<ObjetivoAprendizaje>()
    private val historialLista = mutableListOf<HistorialAuditoria>()

    // Estados reactivos (StateFlow)
    private val _unidadesState = MutableStateFlow<List<UnidadPedagogica>>(unidadesLista.toList())
    val unidadesState: StateFlow<List<UnidadPedagogica>> = _unidadesState.asStateFlow()

    private val _historialState = MutableStateFlow<List<HistorialAuditoria>>(historialLista.toList())
    val historialState: StateFlow<List<HistorialAuditoria>> = _historialState.asStateFlow()

    // --- ACCIONES DE JEFATURA UTP ---

    // Crear o Editar una Unidad Pedagógica
    fun guardarOActualizarUnidad(
        id: String? = null,
        asignatura: AsignaturaUTP,
        titulo: String,
        descripcion: String
    ) {
        if (id != null) {
            val index = unidadesLista.indexOfFirst { it.id == id }
            if (index != -1) {
                unidadesLista[index] = unidadesLista[index].copy(
                    titulo = titulo,
                    descripcion = descripcion,
                    timestamp = System.currentTimeMillis()
                )
                registrarLog(id, "EDITAR", "Se actualizó la unidad '$titulo'")
            }
        } else {
            val nuevaId = UUID.randomUUID().toString()
            val nuevaUnidad = UnidadPedagogica(
                id = nuevaId,
                asignatura = asignatura,
                numero = unidadesLista.count { it.asignatura == asignatura } + 1,
                titulo = titulo,
                descripcion = descripcion,
                estado = EstadoContenido.BORRADOR
            )
            unidadesLista.add(nuevaUnidad)
            registrarLog(nuevaId, "CREAR", "Se creó la unidad '$titulo' para ${asignatura.displayName}")
        }
        notificarCambios()
    }

    // Cambiar estados: Aprobar, Rechazar con observaciones, Publicar y Deshabilitar
    fun cambiarEstadoUnidad(
        unidadId: String,
        nuevoEstado: EstadoContenido,
        observaciones: String? = null
    ) {
        val index = unidadesLista.indexOfFirst { it.id == unidadId }
        if (index != -1) {
            val unidadActual = unidadesLista[index]
            unidadesLista[index] = unidadActual.copy(
                estado = nuevoEstado,
                observacionesUTP = observaciones,
                timestamp = System.currentTimeMillis()
            )

            val detalleLog = when (nuevoEstado) {
                EstadoContenido.APROBADO -> "Unidad '${unidadActual.titulo}' APROBADA."
                EstadoContenido.RECHAZADO -> "Unidad '${unidadActual.titulo}' RECHAZADA. Obs: $observaciones"
                EstadoContenido.PUBLICADO -> "Unidad '${unidadActual.titulo}' PUBLICADA."
                EstadoContenido.DESHABILITADO -> "Unidad '${unidadActual.titulo}' DESHABILITADA."
                else -> "Estado de '${unidadActual.titulo}' actualizado a $nuevoEstado."
            }

            registrarLog(unidadId, nuevoEstado.name, detalleLog)
            notificarCambios()
        }
    }

    // Registrar Objetivos de Aprendizaje con multimedias
    fun agregarObjetivoAprendizaje(
        unidadId: String,
        codigoOA: String,
        explicacionEscrita: String,
        audioUrl: String? = null,
        imagenUrl: String? = null
    ) {
        val nuevoOA = ObjetivoAprendizaje(
            id = UUID.randomUUID().toString(),
            unidadId = unidadId,
            codigoOA = codigoOA,
            explicacionEscrita = explicacionEscrita,
            audioUrl = audioUrl,
            imagenUrl = imagenUrl
        )
        oasLista.add(nuevoOA)
        registrarLog(unidadId, "REGISTRAR_OA", "Se agregó el $codigoOA con material multimedia.")
        notificarCambios()
    }

    // Método de auditoría interna
    private fun registrarLog(entidadId: String, accion: String, detalle: String) {
        historialLista.add(0, HistorialAuditoria(
            id = UUID.randomUUID().toString(),
            entidadId = entidadId,
            accion = accion,
            detalle = detalle
        ))
    }

    private fun notificarCambios() {
        _unidadesState.value = unidadesLista.toList()
        _historialState.value = historialLista.toList()
    }
}