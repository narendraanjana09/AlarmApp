package com.nsa.alarmapp.alarm

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.nsa.alarmapp.MainActivity
import com.nsa.alarmapp.Util
import com.nsa.alarmapp.Util.NOTIFICATION_ID
import com.nsa.alarmapp.db.AlarmDao
import com.nsa.alarmapp.db.AlarmDatabase
import com.nsa.alarmapp.db.AlarmModel
import com.nsa.alarmapp.repo.AlarmRepository
import com.nsa.alarmapp.viewmodel.AlarmViewModel
import com.nsa.alarmapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
            val requestCode = intent.extras!!.getInt("requestCode")
        Log.e("TAG", "onReceive: $requestCode", )

            val dao = AlarmDatabase.getDatabase(context).alarmDao()
            val repository = AlarmRepository(dao)

            CoroutineScope(Dispatchers.IO).launch {
                val alarm = repository.getAlarmById(requestCode)
                CoroutineScope(Dispatchers.Main).launch {

                    val pair=Util.checkRepeatList(alarm.repeatList)
                    if(!pair.first && !pair.second){
                        alarm.isOn=false
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.update(alarm)
                        }
                    }else{
                        Util.checkAlarmAndSetAccordingly(alarm,context,true)
                    }

                    val notificationUtils = NotificationUtils(context, alarm)
                    val notification = notificationUtils.getNotificationBuilder().build()
                    notificationUtils.getManager().notify(NOTIFICATION_ID, notification)
                }

            }

    }
}