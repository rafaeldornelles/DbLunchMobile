package br.com.dbserver.lista.dblunch

import android.app.Dialog
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.ViewModel.RestaurantViewModel
import br.com.dbserver.lista.dblunch.ViewModel.RestaurantViewModelFactory
import br.com.dbserver.lista.dblunch.adapter.RestaurantAdapter
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class RestaurantActivity : AppCompatActivity(), RestaurantAdapter.RestaurantListener {

    private val rvRestaurants by lazy {
        findViewById<RecyclerView>(R.id.activity_restaurant_list)
    }

    private var adapterLongClickPosition: Int = 0
    val restaurantAdapter = RestaurantAdapter(ArrayList(), this)


    private val viewModel by lazy {ViewModelProviders.of(this, RestaurantViewModelFactory(application)).get(RestaurantViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        title = "Restaurantes"

        rvRestaurants.adapter = restaurantAdapter
    }

    override fun onResume() {
        super.onResume()
        loadRestaurants()
    }

    private fun loadRestaurants() {
        viewModel.getRestaurants().observe(this, Observer {
            restaurantAdapter.setRestaurants(it)
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

    fun showRestaurantForm(res:Restaurant?, editmode:Boolean = false, checkErrors: Boolean =false){
        AlertDialog.Builder(this).apply {
            setTitle(if (editmode) "Editar restaurante" else "Incluir um restaurante")
            val view = layoutInflater.inflate(R.layout.dialog_restaurant_form, null)
            setView(view)
            val etRestaurantName = view.findViewById<EditText>(R.id.dialog_restaurant_form_name)
            val etRestaurantAddress = view.findViewById<EditText>(R.id.dialog_restaurant_form_address)

            etRestaurantAddress.setText(res?.address)
            etRestaurantName.setText(res?.name)

            if (checkErrors){
                view.findViewById<TextView>(R.id.dialog_restaurant_form_name_error).visibility = if (etRestaurantName.text.isEmpty()) View.VISIBLE else View.INVISIBLE
                view.findViewById<TextView>(R.id.dialog_restaurant_form_address_error).visibility = if (etRestaurantAddress.text.isEmpty()) View.VISIBLE else View.INVISIBLE
            }

            setPositiveButton("Ok") { dialogInterface, i ->
                val restaurant = Restaurant(res?.id ?: 0, etRestaurantName.text.toString(), etRestaurantAddress.text.toString())
                if (etRestaurantAddress.text.isEmpty()||etRestaurantName.text.isEmpty()){
                    showRestaurantForm(restaurant, editmode, true)
                    return@setPositiveButton
                }
                CoroutineScope(IO).launch {
                    viewModel.insertRestaurant(restaurant)
                }
                loadRestaurants()
            }
            setNegativeButton("Cancelar", null)
            show()
        }
    }

    override fun onEditRestaurant(restaurant: Restaurant) {
        showRestaurantForm(restaurant, true)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_delete, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_delete -> {
                val restaurant = restaurantAdapter.restaurants[adapterLongClickPosition]
                AlertDialog.Builder(this).apply {
                    setTitle("Remover Restaurante")
                    setMessage("Tem certeza de que deseja remover o restaurante ${restaurant.name}?")
                    setPositiveButton("Sim") { dialogInterface, i ->
                        removeRestaurant(restaurant)
                    }
                    setNegativeButton("Não", null)
                    show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun removeRestaurant(restaurant: Restaurant){
        CoroutineScope(IO).launch {
            viewModel.deleteRestaurant(restaurant)
        }
        loadRestaurants()
    }

    override fun onContextMenuShown(position: Int) {
        adapterLongClickPosition = position
    }
}
