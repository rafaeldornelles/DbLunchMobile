package br.com.dbserver.lista.dblunch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.dbserver.lista.dblunch.Constantes.DATE_EXTRA
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModel
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

class VotingActivity : AppCompatActivity() {
    val date = MutableLiveData(Date())
    val btnVote by lazy { findViewById<Button>(R.id.activity_voting_button_vote) }
    val viewModel by lazy { ViewModelProviders.of(this, VotingViewModelFactory(MutableLiveData(Date()), application)).get(VotingViewModel::class.java) }
    val lvRestaurant by lazy { findViewById<ListView>(R.id.activity_voting_list) }


    val restaurantAdapter by lazy {  ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList<String>()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        lvRestaurant.adapter = restaurantAdapter

        viewModel.restaurants.observe(this, Observer { reslist ->
            restaurantAdapter.addAll(reslist.map { rest ->
                rest.name
            })
            restaurantAdapter.notifyDataSetChanged()
        })

        btnVote.setOnClickListener {
            val intent = Intent(this, VotingFormActivity::class.java).apply {
                putExtra(DATE_EXTRA, date.value)
            }
            startActivity(intent)
        }
    }
}
