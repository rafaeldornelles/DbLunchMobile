package br.com.dbserver.lista.dblunch.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.db.DbLunchDatabase

class WorkerViewModel(val app: Application): ViewModel() {
    val dao = DbLunchDatabase.getInstance(app).workerDao()

    fun getWorkers() = dao.all()

    suspend fun insertWorker(vararg worker: Worker){
        dao.insert(*worker)
    }

    suspend fun deleteWorker(vararg worker: Worker){
        dao.delete(*worker)
    }
}
class WorkerViewModelFactory(val app:Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerViewModel::class.java)) {
            return WorkerViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}