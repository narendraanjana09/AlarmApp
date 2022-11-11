package com.nsa.alarmapp.db


import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nsa.alarmapp.ui.model.WeekDayModel
import java.io.Serializable

@Entity(tableName ="alarms")
data class AlarmModel(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "alarm_name") val alarmName: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "isOn") var isOn:Boolean?,
    val repeatList:List<WeekDayModel>,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        TODO("repeatList")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(alarmName)
        parcel.writeString(time)
        parcel.writeValue(isOn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlarmModel> {
        override fun createFromParcel(parcel: Parcel): AlarmModel {
            return AlarmModel(parcel)
        }

        override fun newArray(size: Int): Array<AlarmModel?> {
            return arrayOfNulls(size)
        }
    }
}