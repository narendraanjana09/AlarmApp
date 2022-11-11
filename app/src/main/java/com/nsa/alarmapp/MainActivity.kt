package com.nsa.alarmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nsa.alarmapp.alarm.AlarmReceiver
import com.nsa.alarmapp.alarm.NotificationUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}