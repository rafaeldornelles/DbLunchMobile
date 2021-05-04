package br.com.dbserver.lista.dblunch.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Restaurant (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name: String,
    val address: String
)