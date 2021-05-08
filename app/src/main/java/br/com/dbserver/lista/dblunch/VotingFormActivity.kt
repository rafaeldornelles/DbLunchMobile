package br.com.dbserver.lista.dblunch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Constantes.DATE_EXTRA
import br.com.dbserver.lista.dblunch.Model.Vote
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModel
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModelFactory
import br.com.dbserver.lista.dblunch.adapter.RestaurantFormAdapter
import br.com.dbserver.lista.dblunch.adapter.WorkerSpinnerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class VotingFormActivity : AppCompatActivity() {
    val rvRestaurant by lazy { findViewById<RecyclerView>(R.id.activity_voting_form_lista) }
    val viewModel:VotingViewModel by lazy {
        ViewModelProviders.of(this, VotingViewModelFactory(application)).get(VotingViewModel::class.java)
    }
    val restaurantFormAdapter = RestaurantFormAdapter(ArrayList())

    val spWorkers by lazy { findViewById<Spinner>(R.id.activity_voting_form_spinner) }
    val workerSpinnerAdapter by lazy { WorkerSpinnerAdapter(this, ArrayList<Worker>())}
    val btSubmit by lazy { findViewById<Button>(R.id.activity_voting_form_submit) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting_form)
        title = "Votação"
        viewModel.date.value = intent.getSerializableExtra(DATE_EXTRA) as? LocalDate ?: LocalDate.now()
        rvRestaurant.adapter = restaurantFormAdapter

        spWorkers.adapter = workerSpinnerAdapter


        setSubmitButton()
    }

    private fun setSubmitButton() {
        btSubmit.setOnClickListener {
            val selectedRestaurant = restaurantFormAdapter.getSelectedRestaurant()
            val selectedWorker = workerSpinnerAdapter.getItemAt(spWorkers.selectedItemPosition)
            if (selectedRestaurant != null && selectedWorker != null) {
                //Toast.makeText(this, "Selecionado: ${viewModel.selectedRestaurant()!!.name} e ${getSelectedWorker()!!.name}", Toast.LENGTH_LONG).show()
                val vote = Vote(0, viewModel.date.value!!, selectedWorker.id, selectedRestaurant.id)
                CoroutineScope(IO).launch {
                    viewModel.registerVote(vote)
                }

                finish()
                Toast.makeText(this, "Voto inserido com sucesso", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Selecione um restaurante e um usuário", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadSpWorkers() {


    }
    override fun onResume() {
        loadScreen()
        super.onResume()
    }

    fun loadScreen(){
        viewModel.getworkers().observe(this, Observer {list ->
            workerSpinnerAdapter.setWorkers(list)
        })

        viewModel.getRestaurants().observe(this, Observer {
            restaurantFormAdapter.setRestaurants(it)
        })
    }
}
