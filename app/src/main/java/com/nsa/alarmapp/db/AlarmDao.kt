package com.nsa.alarmapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms ORDER BY isOn DESC")
    fun getAll(): LiveData<List<AlarmModel>>

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getById(id: Int): AlarmModel

    @Insert
    suspend fun insert(alarmModel: AlarmModel):Long

    @Update
    suspend fun update(alarmModel: AlarmModel)

    @Delete
    suspend fun delete(alarmModel: AlarmModel)

}