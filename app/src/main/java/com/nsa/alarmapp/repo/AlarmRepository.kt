package com.nsa.alarmapp.repo

import androidx.lifecycle.LiveData
import com.nsa.alarmapp.db.AlarmDao
import com.nsa.alarmapp.db.AlarmModel
import kotlinx.coroutines.flow.Flow

class AlarmRepository(val alarmDao:AlarmDao) {
    fun getAll(): LiveData<List<AlarmModel>> {
        return alarmDao.getAll()
    }
    suspend fun insert(alarmModel: AlarmModel): Long {
        return alarmDao.insert(alarmModel)
    }

    suspend fun update(alarmModel: AlarmModel) {
        alarmDao.update(alarmModel)
    }

    suspend fun delete(alarmModel: AlarmModel) {
        alarmDao.delete(alarmModel)
    }

    suspend fun getAlarmById(requestCode: Int): AlarmModel {
       return alarmDao.getById(requestCode)
    }

}