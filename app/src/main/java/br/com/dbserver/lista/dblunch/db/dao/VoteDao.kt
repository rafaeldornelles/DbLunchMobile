package br.com.dbserver.lista.dblunch.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import br.com.dbserver.lista.dblunch.Model.Vote
import br.com.dbserver.lista.dblunch.Model.VotePopulated
import java.time.LocalDate
import java.util.*

@Dao
interface VoteDao {
    @Query("SELECT * FROM Vote")
    fun all(): LiveData<List<Vote>>

    @Transaction
    @Query("SELECT * FROM Vote")
    fun allPopulated(): LiveData<List<VotePopulated>>

    @Query("SELECT * FROM Vote WHERE date = :date")
    fun getVotesInDate(date: LocalDate): LiveData<List<Vote>>

    @Query("SELECT * FROM Vote WHERE date = :date AND restaurantId = :restaurantId")
    fun getVotesInDateByRestaurant(date: LocalDate, restaurantId:Int ): LiveData<List<VotePopulated>>

    @Query("DELETE FROM Vote")
    fun clear()

    @Transaction
    @Query("SELECT * FROM Vote WHERE date = :date")
    fun getVotesInDatePopulated(date: LocalDate): LiveData<List<VotePopulated>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg vote:Vote)

    @Delete
    suspend fun delete(vararg vote: Vote)
}