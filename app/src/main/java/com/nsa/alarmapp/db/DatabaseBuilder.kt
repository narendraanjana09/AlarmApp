package com.nsa.alarmapp.db

import android.content.Context
import androidx.room.Room

    object DatabaseBuilder {
        private var INSTANCE: AlarmDatabase? = null
        fun getInstance(context: Context): AlarmDatabase {
            if (INSTANCE == null) {
                synchronized(AlarmDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AlarmDatabase::class.java,
                "alarm_db"
            ).build()
    }