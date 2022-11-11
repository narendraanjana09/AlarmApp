package com.nsa.alarmapp.alarm

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager.STREAM_RING
import android.media.AudioManager.STREAM_VOICE_CALL
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.nsa.alarmapp.MainActivity
import com.nsa.alarmapp.R
import com.nsa.alarmapp.Util
import com.nsa.alarmapp.Util.NOTIFICATION_ID
import com.nsa.alarmapp.db.AlarmModel

class  NotificationUtils(base: Context,val alarmModel: AlarmModel) : ContextWrapper(base) {

    val MYCHANNEL_ID = "Alarm App Alert"
    val MYCHANNEL_NAME = "Alarm App Notification"


    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    // Create channel for Android version 26+
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel = NotificationChannel(MYCHANNEL_ID, MYCHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)

        getManager().createNotificationChannel(channel)
    }

    // Get Manager
    fun getManager() : NotificationManager {
        if (manager == null) manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val snoozeActionIntent = Intent(this, SnoozeReceiver::class.java)
        snoozeActionIntent.putExtra("id",alarmModel.id)

        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, alarmModel.id, snoozeActionIntent, 0)



        return NotificationCompat.Builder(applicationContext, MYCHANNEL_ID)
            .setContentTitle(alarmModel.alarmName)
            .setContentText(Util.convertTo12Hours(alarmModel.time!!)+" 🔔🔔")
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setColor(Color.WHITE)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", snoozePendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),STREAM_RING)
            .setAutoCancel(true)
    }

}