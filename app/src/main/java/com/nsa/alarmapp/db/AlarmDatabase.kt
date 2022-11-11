package com.nsa.alarmapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nsa.alarmapp.db.json.Converter

@Database(entities = [AlarmModel::class], version = 1)
@TypeConverters(Converter::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    companion object {

        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): AlarmDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AlarmDatabase::class.java,
                "alarm_database"
            )
                .addTypeConverter(Converter())
                .build()
        }
    }
}