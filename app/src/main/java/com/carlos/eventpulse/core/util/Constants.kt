package com.carlos.eventpulse.core.util

object Constants {
    const val BASE_URL = "https://eventpulse.danielsa.icu/api/v1/"
    const val WS_BASE_URL = "wss://eventpulse.danielsa.icu/ws?token="
    const val PREFS_NAME = "eventpulse_prefs"
    const val KEY_TOKEN = "jwt_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_ROL = "user_rol"
    const val KEY_EVENTO_ID = "evento_id"

    // WebSocket event types
    const val WS_INCIDENCIA_NUEVA = "incidencia_nueva"
    const val WS_INCIDENCIA_ACTUALIZADA = "incidencia_actualizada"
    const val WS_TAREA_NUEVA = "tarea_nueva"
    const val WS_TAREA_ACTUALIZADA = "tarea_actualizada"
    const val WS_MENSAJE_NUEVO = "mensaje_nuevo"
    const val WS_EVENTO_TERMINADO = "evento_terminado"
    const val WS_PING = "ping"

    // Notification channels
    const val CHANNEL_CHAT = "channel_chat"
    const val CHANNEL_INCIDENCIAS = "channel_incidencias"
    const val CHANNEL_TAREAS = "channel_tareas"

    // Notification IDs
    const val NOTIF_CHAT_ID = 1001
    const val NOTIF_INCIDENCIA_ID = 1002
    const val NOTIF_TAREA_ID = 1003

    // Deep link args
    const val DEEP_LINK_TYPE = "deep_link_type"
    const val DEEP_LINK_ID = "deep_link_id"
    const val DEEP_LINK_CHAT = "chat"
    const val DEEP_LINK_INCIDENCIA = "incidencia"
    const val DEEP_LINK_TAREA = "tarea"
}
