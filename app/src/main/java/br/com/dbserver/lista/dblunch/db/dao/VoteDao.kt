package br.com.dbserver.lista.dblunch.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import br.com.dbserver.lista.dblunch.Model.Vote

@Dao
interface VoteDao {
    @Query("SELECT * FROM Vote")
    fun all(): LiveData<List<Vote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg vote:Vote)

    @Delete
    suspend fun delete(vararg vote: Vote)
}