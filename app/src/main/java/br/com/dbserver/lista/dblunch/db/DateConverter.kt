package br.com.dbserver.lista.dblunch.db

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DateConverters {
    @TypeConverter
    fun dateToString(date: LocalDate): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(formatter)
    }

    @TypeConverter
    fun stringToDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }
}