package br.com.dbserver.lista.dblunch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.R

class RestaurantAdapter(val restaurants: ArrayList<Restaurant>, val listener: RestaurantListener) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
        holder.itemView.setOnClickListener{
            listener.onEditRestaurant(restaurant)
        }

        holder.itemView.setOnCreateContextMenuListener(listener)
        holder.itemView.setOnLongClickListener {
            listener.onContextMenuShown(position)
            false
        }


    }

    fun setRestaurants(newRestaurants: List<Restaurant>) {
        restaurants.clear()
        restaurants.addAll(newRestaurants)
        notifyDataSetChanged()
    }

    class RestaurantViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvName: TextView = v.findViewById(R.id.item_restaurant_name)
        private val tvAddress: TextView = v.findViewById(R.id.item_restaurant_address)

        fun bind(restaurant: Restaurant){
            tvName.text = restaurant.name
            tvAddress.text = restaurant.address
        }
    }

    interface RestaurantListener: View.OnCreateContextMenuListener{
        fun onEditRestaurant(restaurant: Restaurant)
        fun onContextMenuShown(position: Int)
    }
}