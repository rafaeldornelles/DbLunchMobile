package br.com.dbserver.lista.dblunch.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Vote (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Date,
    @ForeignKey(entity = Worker::class, childColumns = ["workerId"], parentColumns = ["id"])
    val workerId: Int,
    @ForeignKey(entity = Restaurant::class, childColumns = ["restaurantId"], parentColumns = ["id"])
    val restaurantId: Int
)