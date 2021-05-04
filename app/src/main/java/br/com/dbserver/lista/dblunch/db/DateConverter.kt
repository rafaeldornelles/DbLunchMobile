package br.com.dbserver.lista.dblunch.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class DateConverters {
    @TypeConverter
    fun dateToString(date: Date): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(date)
    }

    @TypeConverter
    fun stringToDate(date: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.parse(date) ?: Date()
    }
}