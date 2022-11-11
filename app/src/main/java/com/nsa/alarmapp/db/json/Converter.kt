package com.nsa.alarmapp.db.json

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsa.alarmapp.ui.model.WeekDayModel

@ProvidedTypeConverter
class Converter  {
    @TypeConverter
    fun toWeekDayJson(meaning: List<WeekDayModel>) : String {
        return Gson().toJson(
            meaning,
            object : TypeToken<ArrayList<WeekDayModel>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromWeekDayJson(json: String): List<WeekDayModel>{
        return Gson().fromJson<ArrayList<WeekDayModel>>(
            json,
            object: TypeToken<ArrayList<WeekDayModel>>(){}.type
        ) ?: emptyList()
    }
}