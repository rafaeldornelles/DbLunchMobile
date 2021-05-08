package br.com.dbserver.lista.dblunch.Model

import androidx.room.Embedded
import androidx.room.Relation

data class VotePopulated (
    @Embedded
    val vote: Vote?,
    @Relation(
        entityColumn = "id",
        parentColumn = "restaurantId"
    )
    val restaurant: Restaurant?,
    @Relation(
        entityColumn = "id",
        parentColumn = "workerId"
    )
    val worker: Worker?
)