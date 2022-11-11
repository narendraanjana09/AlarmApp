package com.nsa.alarmapp

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nsa.alarmapp.alarm.AlarmReceiver
import com.nsa.alarmapp.db.AlarmModel
import com.nsa.alarmapp.ui.model.WeekDayModel
import java.text.SimpleDateFormat
import java.util.*

object Util {

   val NOTIFICATION_ID=150
    fun cancelAlarm(id:Int,activity:Activity) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, id, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.cancel(pendingIntent)

        Log.e("AlarmCon", "cancelAlarm: ", )
    }

     fun convertTo12Hours(militaryTime: String): String{
        //in => "14:00:00"
        //out => "02:00 PM"
        val inputFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val date = inputFormat.parse(militaryTime)
        return outputFormat.format(date)
    }

    fun setExactAlarm(id: Int, time: String, day:Int,context: Context) {
        val calendar = calendar(time,day)

        val (alarmManager, pendingIntent) = getAlarmManAndPendInten(context, id)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Log.e("AlarmCon", "setExactAlarm: day:$day", )
    }
    private fun setDailyAlarm(id: Int, time: String, context: Context) {
        val calendar = calendar(time,-1)
        val (alarmManager, pendingIntent) = getAlarmManAndPendInten(context, id)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent)

        Log.e("AlarmCon", "setDailyAlarm: ", )
    }

    private fun calendar(time: String,day:Int): Calendar {
        val calendar = Calendar.getInstance()
        if(day!=-1){
            calendar.set(Calendar.DAY_OF_WEEK,day)
            Log.e("TAG", "calendar: day of week $day ", )
        }

        calendar.set(Calendar.HOUR_OF_DAY, time.split(":")[0].toInt())
        calendar.set(Calendar.MINUTE, time.split(":")[1].toInt())
        calendar.set(Calendar.SECOND, 0)

        if(calendar.timeInMillis < System.currentTimeMillis()) {
            Log.e("calendar", "Was In past: ", )
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }

        return calendar
    }

    private fun setWeekDayRepeatAlarm(id: Int, time: String, context: Context, day:Int) {
        val calendar = calendar(time,day)

        val (alarmManager, pendingIntent) = getAlarmManAndPendInten(context, id)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY*7,pendingIntent)

        Log.e("AlarmCon", "setWeekDayRepeatAlarm $day: ", )
    }

    private fun getAlarmManAndPendInten(
        context: Context,
        id: Int
    ): Pair<AlarmManager, PendingIntent> {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("requestCode", id)
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE)
        return Pair(alarmManager, pendingIntent)
    }

    fun checkRepeatList(weekDayList: List<WeekDayModel>): Pair<Boolean, Boolean> {

        var allDays = true
        var singleDays = false
        weekDayList.forEach {
            if (it.selected) {
                singleDays = true
            } else {
                allDays = false
            }
        }

        return Pair(allDays, singleDays)

    }

    fun checkAlarmAndSetAccordingly(alarmModel: AlarmModel, context: Context,fromService:Boolean) {


        val pair=checkRepeatList(alarmModel.repeatList)
        if(pair.first || pair.second){
            if(pair.first){
                //setDailyAlarm(alarmModel.id,alarmModel.time!!,context)
                setExactAlarm(alarmModel.id,alarmModel.time!!,-1,context)
            }else{
                if(fromService){

                    val calendar=Calendar.getInstance()
                    val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
                    setExactAlarm(alarmModel.id,alarmModel.time!!,day,context)
                    Log.e("TAG", "setExactAlarm:from service ", )

                }else{
                alarmModel.repeatList.forEach{
                    var day=0
                    if(it.selected){
                    when(it.day){
                        "Mon"->{
                            day=Calendar.MONDAY
                        }
                        "Tue"->{
                            day=Calendar.TUESDAY
                        }
                        "Wed"->{
                            day=Calendar.WEDNESDAY
                        }
                        "Thu"->{
                            day=Calendar.THURSDAY
                        }
                        "Fri"->{
                            day=Calendar.FRIDAY
                        }
                        "Sat"->{
                            day=Calendar.SATURDAY
                        }
                        "Sun"->{
                            day=Calendar.SUNDAY
                        }
                    }
                        setExactAlarm(alarmModel.id,alarmModel.time!!,day,context)
                        //setWeekDayRepeatAlarm(alarmModel.id,alarmModel.time!!,context,day)
                    }
                    }
                }

            }

        }else{
            if(!fromService){
            setExactAlarm(alarmModel.id,alarmModel.time!!,-1,context)
            }
        }
    }
    fun getDaysString(list:List<WeekDayModel>): String {
        var days="• "
        var alldays=true;
        list.forEach {
            if(it.selected){
                days+=it.day+", "
            }else{
                alldays=false
            }
        }
        if(alldays){
            return "• All Days"
        }
        if(days.contains(",")){
            days=days.substring(0,days.length-2)
        }else{
            return "• Today"
        }

        return days
    }
}