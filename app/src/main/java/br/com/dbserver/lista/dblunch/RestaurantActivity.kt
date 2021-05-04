package br.com.dbserver.lista.dblunch

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.adapter.RestaurantAdapter
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class RestaurantActivity : AppCompatActivity() {
    val rvRestaurants by lazy {
        findViewById<RecyclerView>(R.id.activity_restaurant_list)
    }
    val restaurants = ArrayList<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        title = "Restaurantes"

        rvRestaurants.adapter = RestaurantAdapter(restaurants)
        DbLunchDatabase.getInstance(applicationContext).restaurantDao().all().observe(this, Observer {
            restaurants.clear()
            restaurants.addAll(it)
            rvRestaurants.adapter?.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_restauant, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_add -> showRestaurantForm(null)
        }
        return super.onOptionsItemSelected(item)
    }

    fun showRestaurantForm(res:Restaurant?){
        AlertDialog.Builder(this).apply {
            setTitle("Incluir um restaurante")
            val view = layoutInflater.inflate(R.layout.dialog_restaurant_form, null)
            setView(view)
            val etRestaurantName = view.findViewById<EditText>(R.id.dialog_restaurant_form_name)
            val etRestaurantAddress = view.findViewById<EditText>(R.id.dialog_restaurant_form_address)
            etRestaurantName.setText(res?.name)
            etRestaurantAddress.setText(res?.address)

            setPositiveButton("Ok") { dialogInterface, i ->
                val restaurant = Restaurant(res?.id ?: 0, etRestaurantName.text.toString(), etRestaurantAddress.text.toString())
                if (etRestaurantAddress.text.isEmpty()||etRestaurantName.text.isEmpty()){
                    showRestaurantForm(restaurant)
                    return@setPositiveButton
                }

                CoroutineScope(IO).launch {
                    DbLunchDatabase.getInstance(applicationContext).restaurantDao().insert(restaurant)
                }

                restaurants.add(restaurant)
                rvRestaurants.adapter?.notifyDataSetChanged()
            }
            setNegativeButton("Cancelar", null)
            show()
        }
    }
}
