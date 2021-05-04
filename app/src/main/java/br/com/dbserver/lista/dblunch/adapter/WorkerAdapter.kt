package br.com.dbserver.lista.dblunch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.R

class WorkerAdapter(val workers: ArrayList<Worker>): RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_worker, parent, false)
        return WorkerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workers.size
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val worker = workers[position]
        holder.bind(worker)
    }


    class WorkerViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.item_worker_name)
        val tvFunction = view.findViewById<TextView>(R.id.item_worker_function)

        fun bind(worker:Worker){
            tvName.setText(worker.name)
            tvFunction.setText(worker.function)
        }
    }
}