package br.com.dbserver.lista.dblunch.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import br.com.dbserver.lista.dblunch.Model.Restaurant

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM Restaurant")
    fun all() : LiveData<List<Restaurant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg restaurant: Restaurant)

    @Delete
    suspend fun delete(vararg restaurant: Restaurant)

    @Query("DELETE FROM Restaurant")
    suspend fun clear()
}