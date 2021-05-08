package br.com.dbserver.lista.dblunch.Model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Restaurant (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name: String,
    val address: String)
{
    @Ignore
    var selected = false
}