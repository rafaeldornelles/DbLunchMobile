package br.com.dbserver.lista.dblunch.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.R
import kotlinx.android.synthetic.main.item_spinner_workers.view.*
import org.w3c.dom.Text

class WorkerSpinnerAdapter(context: Context, var workers: ArrayList<Worker>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, workers.map { it.name }) {
    override fun isEnabled(position: Int): Boolean {
        return position>0
    }

    fun getItemAt(position:Int): Worker? {
        return if (position > 0)
            workers.get(position - 1)
        else null
    }

    fun setWorkers(list: List<Worker>) {
        workers = ArrayList(list)
        clear()
        add("Selecione...")
        addAll(workers.map { it.name })
        notifyDataSetChanged()
    }

}

