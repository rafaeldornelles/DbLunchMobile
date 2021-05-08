package br.com.dbserver.lista.dblunch.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.dbserver.lista.dblunch.Model.Restaurant
import br.com.dbserver.lista.dblunch.Model.Vote
import br.com.dbserver.lista.dblunch.Model.Worker
import br.com.dbserver.lista.dblunch.db.dao.RestaurantDao
import br.com.dbserver.lista.dblunch.db.dao.VoteDao
import br.com.dbserver.lista.dblunch.db.dao.WorkerDao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

@Database(entities = [Restaurant::class, Vote::class, Worker::class], version = 3, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class DbLunchDatabase: RoomDatabase(){
    abstract fun restaurantDao(): RestaurantDao
    abstract fun voteDao(): VoteDao
    abstract fun workerDao(): WorkerDao

    companion object{
        private lateinit var instance: DbLunchDatabase

        fun getInstance(context: Context): DbLunchDatabase{
            if (!::instance.isInitialized){
                instance = Room.databaseBuilder(context, DbLunchDatabase::class.java, "dbLunch")
                    .addCallback(populateDbCallback)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance
        }
        private val populateDbCallback = object : Callback(){
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                CoroutineScope(IO).launch {
                    //doPopulate(instance)
                }
            }
        }

        private suspend fun doPopulate(db: DbLunchDatabase){
            if (db.restaurantDao().all().value.isNullOrEmpty()){
                db.restaurantDao().clear()
                db.restaurantDao().insert(
                        Restaurant(0, "Restaurante 1", "Rua Oswaldo Aranha, 64"),
                        Restaurant(0, "Restaurante 2", "Rua Jerônimo de Ornellas, 543"),
                        Restaurant(0, "Restaurante 3", "Rua Venâncio Aires, 450")
                    )
                }
            if (db.workerDao().all().value.isNullOrEmpty()){
                db.workerDao().clear()
                    db.workerDao().insert(
                        Worker(0, "Rafael", "Desenvolvedor"),
                        Worker(0, "Rodrigo", "POO"),
                        Worker(0, "Julia", "Designer")
                    )
                }
            if (db.voteDao().all().value.isNullOrEmpty()) {
                db.voteDao().clear()
            }
        }

    }
}