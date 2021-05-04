package br.com.dbserver.lista.dblunch.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import br.com.dbserver.lista.dblunch.Model.Worker

@Dao
interface WorkerDao {
    @Query("SELECT * FROM Worker")
    fun all():LiveData<List<Worker>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg worker: Worker)

    @Delete
    suspend fun delete(vararg worker: Worker)

    @Query("DELETE FROM worker")
    suspend fun clear()
}