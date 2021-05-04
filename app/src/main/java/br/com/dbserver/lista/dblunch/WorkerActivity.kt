package br.com.dbserver.lista.dblunch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.adapter.WorkerAdapter
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkerActivity : AppCompatActivity() {
    val rvWorkers by lazy { findViewById<RecyclerView>(R.id.activity_worker_list) }
    val workers = ArrayList<Worker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        title = "Colaboradores"

        rvWorkers.adapter = WorkerAdapter(workers)
        DbLunchDatabase.getInstance(applicationContext).workerDao().all().observe(this, Observer {
            workers.clear()
            workers.addAll(it)
            rvWorkers.adapter?.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_worker, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_add -> showWorkerForm(null)
        }
        return super.onOptionsItemSelected(item)
    }

    fun showWorkerForm(worker:Worker?){
        AlertDialog.Builder(this).apply {
            setTitle("Incluir um colaborador")
            val view = layoutInflater.inflate(R.layout.dialog_worker_form, null)
            setView(view)
            val etWorkerName = view.findViewById<EditText>(R.id.dialog_worker_form_name)
            val etWorkerFunction = view.findViewById<EditText>(R.id.dialog_worker_form_function)
            etWorkerName.setText(worker?.name)
            etWorkerFunction.setText(worker?.function)

            setPositiveButton("Ok") { dialogInterface, i ->
                val workerFromForm = Worker(worker?.id ?: 0, etWorkerName.text.toString(), etWorkerFunction.text.toString())
                if (etWorkerName.text.isEmpty()||etWorkerFunction.text.isEmpty()){
                    showWorkerForm(workerFromForm)
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    DbLunchDatabase.getInstance(applicationContext).workerDao().insert(workerFromForm)
                }

                workers.add(workerFromForm)
                rvWorkers.adapter?.notifyDataSetChanged()
            }
            setNegativeButton("Cancelar", null)
            show()
        }
    }
}
