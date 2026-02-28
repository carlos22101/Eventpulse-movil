package com.carlos.eventpulse.features.events.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- Entities ---

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey val id: String,
    val eventoId: String,
    val zonaId: String,
    val zonaNombre: String,
    val tipo: String,
    val descripcion: String,
    val estado: String,
    val creadaPor: String,
    val asignadaA: String?,
    val nombreAsignado: String?,
    val creadaEn: String,
    val actualizadaEn: String
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val eventoId: String,
    val zonaId: String,
    val zonaNombre: String,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val prioridad: String,
    val creadaPor: String,
    val asignadaA: String?,
    val nombreAsignado: String?,
    val completadaEn: String?,
    val creadaEn: String
)

// --- DAO ---

@Dao
interface EventDao {
    // Incidents
    @Query("SELECT * FROM incidents ORDER BY creadaEn DESC")
    fun observeIncidents(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM incidents WHERE id = :id")
    suspend fun getIncident(id: String): IncidentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncidents(incidents: List<IncidentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity)

    // Tasks
    @Query("SELECT * FROM tasks ORDER BY creadaEn DESC")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
}