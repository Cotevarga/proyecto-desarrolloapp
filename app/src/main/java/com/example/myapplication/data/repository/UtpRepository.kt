package com.example.myapplication.data.repository

import com.example.myapplication.data.model.ActividadDesafio
import com.example.myapplication.data.model.AsignaturaUTP
import com.example.myapplication.data.model.EstadoContenido
import com.example.myapplication.data.model.HistorialAuditoria
import com.example.myapplication.data.model.ObjetivoAprendizaje
import com.example.myapplication.data.model.PreguntaPrueba
import com.example.myapplication.data.model.PruebaFinal
import com.example.myapplication.data.model.UnidadPedagogica
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

    private val oasLista = mutableListOf(
        ObjetivoAprendizaje(
            id = "oa1",
            unidadId = "u2",
            codigoOA = "OA 01",
            explicacionEscrita = "Contar y representar cantidades del 1 al 10 mediante objetos concretos e imágenes.",
            audioUrl = "https://ejemplo.cl/audios/conteo_oa01.mp3",
            imagenUrl = "https://ejemplo.cl/imagenes/tarjetas_conteo.png"
        )
    )

    private val actividadesDesafiosLista = mutableListOf(
        ActividadDesafio(
            id = "act1",
            oaId = "oa1",
            titulo = "Practicar: conteo progresivo",
            instrucciones = "Agrupar tarjetas según el número indicado por el docente.",
            esDesafio = false
        ),
        ActividadDesafio(
            id = "des1",
            oaId = "oa1",
            titulo = "Desafío 1: Encontrar el número oculto",
            instrucciones = "Completar la secuencia numérica del 1 al 10 omitiendo un elemento.",
            esDesafio = true
        )
    )

    private val pruebasLista = mutableListOf(
        PruebaFinal(
            id = "p1",
            unidadId = "u2",
            titulo = "Prueba Sumativa Unidad 1 - Matemáticas",
            preguntas = listOf(
                PreguntaPrueba(
                    "preg1",
                    "oa1",
                    "¿Cuántos lápices hay en el grupo?",
                    listOf("3", "5", "8"),
                    1
                ),
                PreguntaPrueba(
                    "preg2",
                    "oa1",
                    "Selecciona el número que sigue al 7",
                    listOf("6", "8", "9"),
                    1
                )
            ),
            estado = EstadoContenido.APROBADO
        )
    )

    private val historialLista = mutableListOf<HistorialAuditoria>()

    // Estados reactivos (StateFlow)
    private val _unidadesState = MutableStateFlow<List<UnidadPedagogica>>(unidadesLista.toList())
    val unidadesState: StateFlow<List<UnidadPedagogica>> = _unidadesState.asStateFlow()

    private val _oasState = MutableStateFlow<List<ObjetivoAprendizaje>>(oasLista.toList())
    val oasState: StateFlow<List<ObjetivoAprendizaje>> = _oasState.asStateFlow()

    private val _actividadesState = MutableStateFlow<List<ActividadDesafio>>(actividadesDesafiosLista.toList())
    val actividadesState: StateFlow<List<ActividadDesafio>> = _actividadesState.asStateFlow()

    private val _pruebasState = MutableStateFlow<List<PruebaFinal>>(pruebasLista.toList())
    val pruebasState: StateFlow<List<PruebaFinal>> = _pruebasState.asStateFlow()

    private val _historialState = MutableStateFlow<List<HistorialAuditoria>>(historialLista.toList())
    val historialState: StateFlow<List<HistorialAuditoria>> = _historialState.asStateFlow()

    // --- MÉTODOS DE GESTIÓN UTP ---

    // 1. Crear o Editar una Unidad Pedagógica
    fun guardarOActualizarUnidad(
        id: String? = null,
        asignatura: AsignaturaUTP,
        titulo: String,
        descripcion: String
    ): String {
        val unidadIdTarget: String
        if (id != null) {
            val index = unidadesLista.indexOfFirst { it.id == id }
            if (index != -1) {
                unidadesLista[index] = unidadesLista[index].copy(
                    titulo = titulo,
                    descripcion = descripcion,
                    timestamp = System.currentTimeMillis()
                )
                registrarLog(id, "EDITAR_UNIDAD", "Se actualizó la unidad '$titulo'")
            }
            unidadIdTarget = id
        } else {
            unidadIdTarget = UUID.randomUUID().toString()
            val nuevaUnidad = UnidadPedagogica(
                id = unidadIdTarget,
                asignatura = asignatura,
                numero = unidadesLista.count { it.asignatura == asignatura } + 1,
                titulo = titulo,
                descripcion = descripcion,
                estado = EstadoContenido.BORRADOR
            )
            unidadesLista.add(nuevaUnidad)
            registrarLog(unidadIdTarget, "CREAR_UNIDAD", "Se creó la unidad '$titulo' para ${asignatura.displayName}")
        }
        notificarCambios()
        return unidadIdTarget
    }

    // 2. Registrar/Editar Objetivos de Aprendizaje (OA) con Explicación Escrita, Audio e Imagen Autorizada
    fun guardarOActualizarOA(
        id: String? = null,
        unidadId: String,
        codigoOA: String,
        explicacionEscrita: String,
        audioUrl: String? = null,
        imagenUrl: String? = null
    ): String {
        val oaIdTarget: String
        if (id != null) {
            val idx = oasLista.indexOfFirst { it.id == id }
            if (idx != -1) {
                oasLista[idx] = oasLista[idx].copy(
                    codigoOA = codigoOA,
                    explicacionEscrita = explicacionEscrita,
                    audioUrl = audioUrl,
                    imagenUrl = imagenUrl
                )
                registrarLog(unidadId, "EDITAR_OA", "Se actualizó el $codigoOA con recursos multimedia")
            }
            oaIdTarget = id
        } else {
            oaIdTarget = UUID.randomUUID().toString()
            val nuevoOA = ObjetivoAprendizaje(
                id = oaIdTarget,
                unidadId = unidadId,
                codigoOA = codigoOA,
                explicacionEscrita = explicacionEscrita,
                audioUrl = audioUrl,
                imagenUrl = imagenUrl
            )
            oasLista.add(nuevoOA)
            registrarLog(unidadId, "REGISTRAR_OA", "Se registró el $codigoOA con explicaciones y multimedia autorizada")
        }
        notificarCambios()
        return oaIdTarget
    }

    // 3. Crear y Modificar Actividades / Desafíos asociados a un OA
    fun guardarActividadDesafio(
        id: String? = null,
        oaId: String,
        titulo: String,
        instrucciones: String,
        esDesafio: Boolean
    ) {
        if (id != null) {
            val idx = actividadesDesafiosLista.indexOfFirst { it.id == id }
            if (idx != -1) {
                actividadesDesafiosLista[idx] = actividadesDesafiosLista[idx].copy(
                    titulo = titulo,
                    instrucciones = instrucciones,
                    esDesafio = esDesafio
                )
                registrarLog(oaId, "EDITAR_ACTIVIDAD", "Se actualizó '${titulo}'")
            }
        } else {
            val nuevaAct = ActividadDesafio(
                id = UUID.randomUUID().toString(),
                oaId = oaId,
                titulo = titulo,
                instrucciones = instrucciones,
                esDesafio = esDesafio
            )
            actividadesDesafiosLista.add(nuevaAct)
            val tipoStr = if (esDesafio) "Desafío" else "Actividad"
            registrarLog(oaId, "CREAR_ACTIVIDAD", "Se creó $tipoStr '$titulo' asociado al OA")
        }
        notificarCambios()
    }

    // 4. Crear o Modificar Prueba Final y Verificar Relación con los OA
    fun guardarPruebaFinal(
        unidadId: String,
        titulo: String,
        preguntas: List<PreguntaPrueba>
    ) {
        val index = pruebasLista.indexOfFirst { it.unidadId == unidadId }
        val idPrueba = if (index != -1) pruebasLista[index].id else UUID.randomUUID().toString()

        val nuevaPrueba = PruebaFinal(
            id = idPrueba,
            unidadId = unidadId,
            titulo = titulo,
            preguntas = preguntas,
            estado = EstadoContenido.PENDIENTE
        )

        if (index != -1) {
            pruebasLista[index] = nuevaPrueba
        } else {
            pruebasLista.add(nuevaPrueba)
        }

        registrarLog(unidadId, "PRUEBA_FINAL", "Se registró/actualizó la prueba final '$titulo' con ${preguntas.size} preguntas.")
        notificarCambios()
    }

    // Verifica la relación pedagógica entre las preguntas de la prueba y los OA de la unidad
    fun verificarRelacionPreguntasOA(unidadId: String): Map<String, Int> {
        val oasUnidad = oasLista.filter { it.unidadId == unidadId }
        val prueba = pruebasLista.find { it.unidadId == unidadId }
        val resumen = mutableMapOf<String, Int>()

        oasUnidad.forEach { oa ->
            val cantidadPreguntas = prueba?.preguntas?.count { it.oaId == oa.id } ?: 0
            resumen[oa.codigoOA] = cantidadPreguntas
        }
        return resumen
    }

    // 5. Cambiar estados (Aprobar, Rechazar, Publicar, Deshabilitar)
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

    private fun registrarLog(entidadId: String, accion: String, detalle: String) {
        historialLista.add(0, HistorialAuditoria(
            id = UUID.randomUUID().toString(),
            entidadId = entidadId,
            accion = accion,
            detalle = detalle
        )
        )
    }

    private fun notificarCambios() {
        _unidadesState.value = unidadesLista.toList()
        _oasState.value = oasLista.toList()
        _actividadesState.value = actividadesDesafiosLista.toList()
        _pruebasState.value = pruebasLista.toList()
        _historialState.value = historialLista.toList()
    }
    fun agregarUnidad(nuevaUnidad: UnidadPedagogica) {
        _unidadesState.value = _unidadesState.value + nuevaUnidad
    }
}