package com.nsa.alarmapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nsa.alarmapp.repo.AlarmRepository

class MainViewModelFactory(private val application: Application):   ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(application) as T
    }
}