package br.com.dbserver.lista.dblunch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Constantes.DATE_EXTRA
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModel
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModelFactory
import br.com.dbserver.lista.dblunch.adapter.RestaurantFormAdapter
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import java.util.*
import kotlin.collections.ArrayList

class VotingFormActivity : AppCompatActivity() {
    val rvRestaurant by lazy { findViewById<RecyclerView>(R.id.activity_voting_form_lista) }
    val date by lazy {
        MutableLiveData(intent.getSerializableExtra(DATE_EXTRA) as? Date ?: Date())
    }
    val viewModel:VotingViewModel by lazy {
        ViewModelProviders.of(this, VotingViewModelFactory(date, application)).get(VotingViewModel::class.java)
    }
    val restaurantFormAdapter = RestaurantFormAdapter(ArrayList())

    val spWorkers by lazy { findViewById<Spinner>(R.id.activity_voting_form_spinner) }
    val workerSpinnerAdapter by lazy { ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrayList())}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting_form)
        title = "Votação"

        rvRestaurant.adapter = restaurantFormAdapter
        viewModel.restaurants.observe(this, Observer {
            restaurantFormAdapter.setRestaurants(it)
        })

        loadSpWorkers()
    }

    private fun loadSpWorkers() {
        spWorkers.adapter = workerSpinnerAdapter
        viewModel.workers.observe(this, Observer {list ->
            workerSpinnerAdapter.clear()
            workerSpinnerAdapter.addAll(list.map{it.name})
            workerSpinnerAdapter.notifyDataSetChanged()
        })

    }

    override fun onResume() {
        super.onResume()
    }
}
