package com.carlos.eventpulse.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlos.eventpulse.features.auth.data.local.UserEntity
import com.carlos.eventpulse.features.events.data.local.EventDao
import com.carlos.eventpulse.features.events.data.local.IncidentEntity
import com.carlos.eventpulse.features.events.data.local.TaskEntity
import com.carlos.eventpulse.features.chat.data.local.MessageEntity
import com.carlos.eventpulse.features.chat.data.local.MessageDao

@Database(
    entities = [
        UserEntity::class,
        IncidentEntity::class,
        TaskEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun messageDao(): MessageDao
}
