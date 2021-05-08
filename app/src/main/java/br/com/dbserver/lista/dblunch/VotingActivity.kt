package br.com.dbserver.lista.dblunch

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Constantes.DATE_EXTRA
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModel
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModelFactory
import br.com.dbserver.lista.dblunch.adapter.RestaurantResultAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class VotingActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    val btnVote by lazy { findViewById<Button>(R.id.activity_voting_button_vote) }
    val viewModel by lazy { ViewModelProviders.of(this, VotingViewModelFactory(application)).get(VotingViewModel::class.java) }
    val rvRestaurants by lazy { findViewById<RecyclerView>(R.id.activity_voting_list) }
    val tvDate by lazy { findViewById<TextView>(R.id.activity_voting_day) }
    val tvActive by lazy { findViewById<TextView>(R.id.activity_voting_active_status) }
    val tvTotalvotesValue by lazy { findViewById<TextView>(R.id.activity_voting_totalVotesValue) }

    val restaurantAdapter by lazy { RestaurantResultAdapter(ArrayList<VotingViewModel.RestaurantInDate>()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)
        title = "Status da votação"

        rvRestaurants.adapter = restaurantAdapter
        val dividerItemDecoration = DividerItemDecoration(rvRestaurants.context, (rvRestaurants.layoutManager as LinearLayoutManager).orientation);
        rvRestaurants.addItemDecoration(dividerItemDecoration)

        btnVote.setOnClickListener {
            val intent = Intent(this, VotingFormActivity::class.java).apply {
                putExtra(DATE_EXTRA, viewModel.date.value)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        loadScreen()
        super.onResume()
    }

    fun loadScreen(){
        if (viewModel.date.value!!.dayOfWeek == DayOfWeek.SATURDAY){
            viewModel.date.value = viewModel.date.value!!.plusDays(2)
        }
        if (viewModel.date.value!!.dayOfWeek == DayOfWeek.SUNDAY){
            viewModel.date.value = viewModel.date.value!!.plusDays(1)
        }

        viewModel.date.observe(this, Observer {
            tvDate.text = it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        })

        viewModel.getRestaurants().observe(this, Observer {restaurants->
            viewModel.getvotesInDate().observe(this, Observer {votes ->
                val restaurantsInDate = ArrayList<VotingViewModel.RestaurantInDate>()
                for (restaurant in restaurants){
                    restaurantsInDate.add(VotingViewModel.RestaurantInDate(restaurant, votes.filter { it.restaurant?.id == restaurant.id }.count(), true, votes.count()))
                }
                restaurantAdapter.setRestaurants(restaurantsInDate)
            })
        })

        viewModel.acceptsVotes().observe(this, Observer {
            this.tvActive.text = if(it) "Ativa" else "Encerrada"
            btnVote.isEnabled = it
        })

        viewModel.getVoteCount().observe(this, Observer {
            tvTotalvotesValue.text = it.toString()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_voting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_change_date -> showDatePicker()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDatePicker(){
        Log.i("AAAA", viewModel.date.value!!.toString())
        DatePickerDialog(this, this, viewModel.date.value!!.year, viewModel.date.value!!.monthValue -1, viewModel.date.value!!.dayOfMonth).apply {
            show()
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = LocalDate.of(year, month +1, dayOfMonth)
        viewModel.date.value = selectedDate
        loadScreen()
    }
}
