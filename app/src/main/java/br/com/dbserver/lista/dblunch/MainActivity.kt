package br.com.dbserver.lista.dblunch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase

class MainActivity : AppCompatActivity() {

    val btVotacoes by lazy {
        findViewById<Button>(R.id.activity_main_button_voting)
    }

    val btRestaurants by lazy {
        findViewById<Button>(R.id.activity_main_button_restaurants)
    }

    val btWorkers by lazy {
        findViewById<Button>(R.id.activity_main_button_workers)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpButtons()
        DbLunchDatabase.getInstance(applicationContext).restaurantDao().all().observe(this, Observer {
            Log.i("AAAA", it.size.toString())
        })
    }

    private fun setUpButtons() {
        btVotacoes.setOnClickListener{
            TODO()
        }

        btRestaurants.setOnClickListener{
            val intent = Intent(this, RestaurantActivity::class.java)
            startActivity(intent)
        }
        btWorkers.setOnClickListener{
            val intent = Intent(this, WorkerActivity::class.java)
            startActivity(intent)
        }
    }
}
