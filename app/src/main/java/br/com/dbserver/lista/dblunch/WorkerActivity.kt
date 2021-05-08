package br.com.dbserver.lista.dblunch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.ViewModel.WorkerViewModel
import br.com.dbserver.lista.dblunch.ViewModel.WorkerViewModelFactory
import br.com.dbserver.lista.dblunch.adapter.WorkerAdapter
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkerActivity : AppCompatActivity(), WorkerAdapter.WorkerListener {
    val rvWorkers by lazy { findViewById<RecyclerView>(R.id.activity_worker_list) }

    private var adapterLongClickPosition: Int = 0
    val workerAdapter = WorkerAdapter(ArrayList(), this)
    val viewModel by lazy { ViewModelProviders.of(this, WorkerViewModelFactory(application)).get(WorkerViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        title = "Colaboradores"

        rvWorkers.adapter = workerAdapter
        val dividerItemDecoration = DividerItemDecoration(rvWorkers.context, (rvWorkers.layoutManager as LinearLayoutManager).orientation);
        rvWorkers.addItemDecoration(dividerItemDecoration)
    }

    override fun onResume() {
        super.onResume()
        loadWorkers()
    }

    private fun loadWorkers() {
        DbLunchDatabase.getInstance(applicationContext).workerDao().all().observe(this, Observer {
            workerAdapter.setWorkers(it)
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

    fun showWorkerForm(worker:Worker?, editmode:Boolean = false, checkErrors:Boolean=false){
        AlertDialog.Builder(this).apply {
            setTitle(if (editmode) "Editar colaborador" else "Incluir um colaborador")
            val view = layoutInflater.inflate(R.layout.dialog_worker_form, null)
            setView(view)
            val etWorkerName = view.findViewById<EditText>(R.id.dialog_worker_form_name)
            val etWorkerFunction = view.findViewById<EditText>(R.id.dialog_worker_form_function)
            etWorkerName.setText(worker?.name)
            etWorkerFunction.setText(worker?.function)

            if (checkErrors){
                view.findViewById<TextView>(R.id.dialog_worker_form_name_error).visibility = if (etWorkerName.text.isEmpty()) View.VISIBLE else View.INVISIBLE
                view.findViewById<TextView>(R.id.dialog_worker_form_function_error).visibility = if (etWorkerFunction.text.isEmpty()) View.VISIBLE else View.INVISIBLE
            }

            setPositiveButton("Ok") { dialogInterface, i ->
                val workerFromForm = Worker(worker?.id ?: 0, etWorkerName.text.toString(), etWorkerFunction.text.toString())
                if (etWorkerName.text.isEmpty()||etWorkerFunction.text.isEmpty()){
                    showWorkerForm(workerFromForm, editmode, true)
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertWorker(workerFromForm)
                }
                loadWorkers()
            }
            setNegativeButton("Cancelar", null)
            show()
        }
    }

    override fun onEditWorker(worker: Worker){
        showWorkerForm(worker, true)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_delete, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_delete -> {
                val worker = workerAdapter.workers[adapterLongClickPosition]
                AlertDialog.Builder(this).apply {
                    setTitle("Remover Colaborador")
                    setMessage("Tem certeza de que deseja remover o colaborador ${worker.name}?")
                    setPositiveButton("Sim") { dialogInterface, i ->
                        removeWorker(worker)
                    }
                    setNegativeButton("Não", null)
                    show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun removeWorker(worker: Worker){
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteWorker(worker)
        }
        loadWorkers()
    }

    override fun onContextMenuShown(position: Int) {
        adapterLongClickPosition = position
    }

}
