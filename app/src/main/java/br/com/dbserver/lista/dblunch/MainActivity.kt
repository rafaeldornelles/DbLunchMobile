package br.com.dbserver.lista.dblunch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase

class MainActivity : AppCompatActivity() {

    val cardVotacoes by lazy {
        findViewById<CardView>(R.id.activity_main_card_votacao)
    }

    val cardRestaurants by lazy {
        findViewById<CardView>(R.id.activity_main_card_restaurantes)
    }

    val cardWorkers by lazy {
        findViewById<CardView>(R.id.activity_main_card_workers)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpButtons()
        DbLunchDatabase.getInstance(applicationContext).restaurantDao().all().observe(this, Observer {
        })
    }

    private fun setUpButtons() {
        cardVotacoes.setOnClickListener{
            val intent = Intent(this, VotingActivity::class.java)
            startActivity(intent)
        }

        cardRestaurants.setOnClickListener{
            val intent = Intent(this, RestaurantActivity::class.java)
            startActivity(intent)
        }
        cardWorkers.setOnClickListener{
            val intent = Intent(this, WorkerActivity::class.java)
            startActivity(intent)
        }
    }
}
