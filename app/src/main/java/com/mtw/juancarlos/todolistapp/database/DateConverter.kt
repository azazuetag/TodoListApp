package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.TypeConverter
import java.util.*


class DateConverter {
    @TypeConverter
    fun toDate(timestamp:Long?): Date?{
        return  if (timestamp != null) Date(timestamp) else null
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): Long? = date?.time

}