package br.com.dbserver.lista.dblunch.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase

class RestaurantViewModel(app:Application): ViewModel() {
    val dao = DbLunchDatabase.getInstance(app).restaurantDao()

    fun getRestaurants() = dao.all()

    suspend fun insertRestaurant(vararg restaurant: Restaurant){
        dao.insert(*restaurant)
    }

    suspend fun deleteRestaurant(vararg restaurant: Restaurant){
        dao.delete(*restaurant)
    }
}

class RestaurantViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantViewModel::class.java)) {
            return RestaurantViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}