package br.com.dbserver.lista.dblunch.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.R
import br.com.dbserver.lista.dblunch.ViewModel.RestaurantViewModel
import br.com.dbserver.lista.dblunch.ViewModel.VotingViewModel
import kotlinx.android.synthetic.main.item_vote_result.*
import java.util.*

class RestaurantResultAdapter(val restaurants: ArrayList<VotingViewModel.RestaurantInDate>): RecyclerView.Adapter<RestaurantResultAdapter.RestaurantResultViewHolder>() {
    init {
        orderRestaurants()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vote_result, parent, false)
        return RestaurantResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantResultViewHolder, position: Int) {
        holder.bind(restaurants[position], position)
    }

    fun setRestaurants(restaurantsInDate: ArrayList<VotingViewModel.RestaurantInDate>) {
        restaurants.clear()
        restaurants.addAll(restaurantsInDate)
        orderRestaurants()
        notifyDataSetChanged()
    }

    fun orderRestaurants(){
        restaurants.sortWith(Comparator { t, t2 ->
            if (t.numberOfVotes == t2.numberOfVotes)
                t.restaurant.name.compareTo(t2.restaurant.name, true)
            else t2.numberOfVotes - t.numberOfVotes
        })
    }


    inner class RestaurantResultViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val tvRestaurantName = v.findViewById<TextView>(R.id.item_vote_result_restaurant)
        val tvVoteCount = v.findViewById<TextView>(R.id.item_vote_result_votes)
        val progressVotes = v.findViewById<ProgressBar>(R.id.item_vote_restaurant_progress)
        val orderLabel = v.findViewById<TextView>(R.id.item_vote_result_position)

        fun bind(res: VotingViewModel.RestaurantInDate, position: Int){
            tvRestaurantName.text = res.restaurant.name
            tvVoteCount.text = res.numberOfVotes.toString()
            progressVotes.progress = if (res.totalVotes>0)  100 * res.numberOfVotes / res.totalVotes else 0
            orderLabel.text= (position + 1).toString()

        }
    }
}