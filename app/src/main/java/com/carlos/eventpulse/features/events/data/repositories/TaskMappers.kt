package com.carlos.eventpulse.features.events.data.repositories

import com.carlos.eventpulse.features.events.data.local.IncidentEntity
import com.carlos.eventpulse.features.events.data.local.TaskEntity
import com.carlos.eventpulse.features.events.data.remote.IncidentDto
import com.carlos.eventpulse.features.events.data.remote.TaskDto
import com.carlos.eventpulse.features.events.domain.model.Incident
import com.carlos.eventpulse.features.events.domain.model.Task

// --- Incident Mappers ---

fun IncidentDto.toEntity() = IncidentEntity(
    id = id, eventoId = eventoId, zonaId = zonaId, zonaNombre = zonaNombre,
    tipo = tipo, descripcion = descripcion, estado = estado, creadaPor = creadaPor,
    asignadaA = asignadaA, nombreAsignado = nombreAsignado,
    creadaEn = creadaEn, actualizadaEn = actualizadaEn
)

fun IncidentEntity.toDomain() = Incident(
    id = id, eventoId = eventoId, zonaId = zonaId, zonaNombre = zonaNombre,
    tipo = tipo, descripcion = descripcion, estado = estado, creadaPor = creadaPor,
    asignadaA = asignadaA, nombreAsignado = nombreAsignado,
    creadaEn = creadaEn, actualizadaEn = actualizadaEn
)

fun IncidentDto.toDomain() = Incident(
    id = id, eventoId = eventoId, zonaId = zonaId, zonaNombre = zonaNombre,
    tipo = tipo, descripcion = descripcion, estado = estado, creadaPor = creadaPor,
    asignadaA = asignadaA, nombreAsignado = nombreAsignado,
    creadaEn = creadaEn, actualizadaEn = actualizadaEn
)

// --- Task Mappers ---

fun TaskDto.toEntity() = TaskEntity(
    id = id, eventoId = eventoId, zonaId = zonaId, zonaNombre = zonaNombre,
    titulo = titulo, descripcion = descripcion, estado = estado, prioridad = prioridad,
    creadaPor = creadaPor, asignadaA = asignadaA, nombreAsignado = nombreAsignado,
    completadaEn = completadaEn, creadaEn = creadaEn
)

fun TaskEntity.toDomain() = Task(
    id = id, eventoId = eventoId, zonaId = zonaId, zonaNombre = zonaNombre,
    titulo = titulo, descripcion = descripcion, estado = estado, prioridad = prioridad,
    creadaPor = creadaPor, asignadaA = asignadaA, nombreAsignado = nombreAsignado,
    completadaEn = completadaEn, creadaEn = creadaEn
)

fun TaskDto.toDomain() = Task(
    id = id, eventoId = eventoId, zonaId = zonaId, zonaNombre = zonaNombre,
    titulo = titulo, descripcion = descripcion, estado = estado, prioridad = prioridad,
    creadaPor = creadaPor, asignadaA = asignadaA, nombreAsignado = nombreAsignado,
    completadaEn = completadaEn, creadaEn = creadaEn
)