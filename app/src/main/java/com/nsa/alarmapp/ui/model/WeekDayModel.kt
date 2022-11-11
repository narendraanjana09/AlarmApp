package com.nsa.alarmapp.ui.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class WeekDayModel(
    @SerializedName("day")
    var day:String,
    @SerializedName("selected")
    var selected:Boolean
)
