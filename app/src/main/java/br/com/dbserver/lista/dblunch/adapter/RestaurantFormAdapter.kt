package br.com.dbserver.lista.dblunch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.R

class RestaurantFormAdapter(val restaurants: ArrayList<Restaurant>): RecyclerView.Adapter<RestaurantFormAdapter.RestaurantFormViewHolder>() {
    override fun onBindViewHolder(holder: RestaurantFormViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantFormViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_form_option, parent, false)
        return RestaurantFormViewHolder(view)
    }

    inner class RestaurantFormViewHolder(v: View): RecyclerView.ViewHolder(v){
            val radiobuttonRestaurant = v.findViewById<CheckBox>(R.id.item_restaurant_form_checkbox)
            val cardRestaurant = v.findViewById<CardView>(R.id.item_restaurant_form_card)


        fun bind(restaurant:Restaurant){
            radiobuttonRestaurant.setText(restaurant.name)
            radiobuttonRestaurant.isChecked = restaurant.selected

            cardRestaurant.setOnClickListener {
                selectRestaurant(restaurant)
            }
        }

        fun selectRestaurant(selectedRestaurant: Restaurant){
            for (restaurant in restaurants){
                restaurant.selected = restaurant.id == selectedRestaurant.id && !restaurant.selected
                notifyDataSetChanged()
            }
        }

        fun getSelectedRestaurant(): Restaurant{
            return restaurants.first{
                it.selected
            }
        }
    }

    fun setRestaurants(newRestaurants: List<Restaurant>){
        restaurants.clear()
        restaurants.addAll(newRestaurants)
        notifyDataSetChanged()
    }

    fun getSelectedRestaurant(): Restaurant? {
        return restaurants.firstOrNull{it.selected}
    }
}