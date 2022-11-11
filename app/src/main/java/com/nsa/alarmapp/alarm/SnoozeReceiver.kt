package com.nsa.alarmapp.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nsa.alarmapp.Util
import com.nsa.alarmapp.db.AlarmDatabase
import com.nsa.alarmapp.repo.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent!!.extras!!.getInt("id")
        Log.e("TAG", "onReceive1: $id")

        val dao = AlarmDatabase.getDatabase(context!!).alarmDao()
        val repository = AlarmRepository(dao)

        CoroutineScope(Dispatchers.IO).launch {
            val alarm = repository.getAlarmById(id)
            CoroutineScope(Dispatchers.Main).launch {

                val df = SimpleDateFormat("HH:mm")
                val time=df.format(Date())

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, time!!.split(":")[0].toInt())
                calendar.set(Calendar.MINUTE, time!!.split(":")[1].toInt())
                calendar.set(Calendar.SECOND, 0)
                calendar.add(Calendar.MINUTE,5)

                val newTime: String = df.format(calendar.time)
                Log.e("TAG", "snooze ${alarm.time} newTime=$newTime: ", )
                Util.setExactAlarm(id,newTime,-1,context)

                alarm.isOn=true
                CoroutineScope(Dispatchers.IO).launch {
                    repository.update(alarm)
                }

            }
        }
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(Util.NOTIFICATION_ID)
    }
}