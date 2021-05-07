package br.com.dbserver.lista.dblunch.ViewModel

import android.app.Application
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.*
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import java.util.*
import kotlin.collections.ArrayList

class VotingViewModel(val date: MutableLiveData<Date>, val application: Application): ViewModel(){
    private val db = DbLunchDatabase.getInstance(application.baseContext)
    private val restaurantDao = db.restaurantDao()
    private val workerDao = db.workerDao()

    val restaurants= restaurantDao.all()
    val workers = workerDao.all()
}

class VotingViewModelFactory(val date: MutableLiveData<Date>, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VotingViewModel::class.java)) {
            return VotingViewModel(date, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}