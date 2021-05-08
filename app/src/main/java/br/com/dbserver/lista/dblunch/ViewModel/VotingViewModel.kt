package br.com.dbserver.lista.dblunch.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.*
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.Model.Vote
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class VotingViewModel(val application: Application): ViewModel() {
    val date = MutableLiveData<LocalDate>(LocalDate.now())
    private val db = DbLunchDatabase.getInstance(application.baseContext)
    private val restaurantDao = db.restaurantDao()
    private val workerDao = db.workerDao()
    private val voteDao = db.voteDao()

    fun getRestaurants() = restaurantDao.all()
    fun getworkers() = workerDao.all()
    fun getVotes() = voteDao.getVotesInDate(date.value ?: LocalDate.now())
    fun getvotesInDate() = voteDao.getVotesInDatePopulated(date.value!!)
    fun getVotesInDateByRestaurant(resId: Int) = voteDao.getVotesInDateByRestaurant(date.value!!,resId)
    fun getVoteCount(): LiveData<Int> = Transformations.map(getvotesInDate()){it.count()}

    fun acceptsVotes() =
        Transformations.switchMap(date) { date ->
            Transformations.map(getAvaliableWorkers()) {
                date.atStartOfDay().plusHours(12).isAfter(LocalDateTime.now()) && it.count() > 0
            }
        }

    fun getAvaliableWorkers(): LiveData<List<Worker>> {
        return Transformations.switchMap(getworkers()){workers ->
            Transformations.map(getvotesInDate()){votes ->
                workers.filter {worker -> votes.none { it.worker?.id ==  worker.id} }
            }
        }
    }


    class RestaurantInDate(val restaurant: Restaurant, val numberOfVotes: Int, val isAvaliable:Boolean, val totalVotes:Int)


    fun selectedRestaurant(): LiveData<Restaurant?> = Transformations.map(getRestaurants()){ restaurant ->
        restaurant.firstOrNull{it.selected}
    }

    suspend fun registerVote(vote: Vote){
        voteDao.insert(vote)
    }
}

class VotingViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VotingViewModel::class.java)) {
            return VotingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}