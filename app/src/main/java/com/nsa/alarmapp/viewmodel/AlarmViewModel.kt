package com.nsa.alarmapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsa.alarmapp.db.AlarmDao
import com.nsa.alarmapp.db.AlarmDatabase
import com.nsa.alarmapp.db.AlarmModel
import com.nsa.alarmapp.repo.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private var dao: AlarmDao
    private var repository: AlarmRepository
    init {
         dao = AlarmDatabase.getDatabase(application.applicationContext).alarmDao()
         repository = AlarmRepository(dao)
    }

    fun getAllAlarms(): LiveData<List<AlarmModel>> {
        return repository.getAll()
    }

     val alarmIdLiveData=MutableLiveData<Int>()
    val alarmLiveData=MutableLiveData<AlarmModel>()

    fun insert(alarmModel: AlarmModel){
        viewModelScope.launch(Dispatchers.IO) {
           val id= repository.insert(alarmModel)
            viewModelScope.launch (Dispatchers.Main){
                alarmIdLiveData.value=id.toInt()
            }
        }
    }

    fun update(alarmModel: AlarmModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(alarmModel)
        }
    }

    fun delete(alarmModel: AlarmModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(alarmModel)
        }
    }

    fun getAlarmBYId(requestCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
           val alarm= repository.getAlarmById(requestCode)

            viewModelScope.launch(Dispatchers.Main) {
                alarmLiveData.value=alarm
            }
        }

    }


}