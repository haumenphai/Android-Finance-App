
package promax.dohaumen.financeapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import promax.dohaumen.financeapp.MyApp.Companion.context
import promax.dohaumen.financeapp.models.MoneyInOut


@Database(entities = [MoneyInOut::class], version = 3)
abstract class MoneyInOutDB : RoomDatabase() {
    abstract fun dao(): MoneyInOutDao

    companion object {
        val get: MoneyInOutDB by lazy {
            synchronized(this) {
                Room.databaseBuilder(context, MoneyInOutDB::class.java, "money_in_out_db")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun insertDemoData() {
            CoroutineScope(Dispatchers.IO).launch {
                MoneyInOut.getDemoMoneyInOut().forEach {
                    get.dao().insert(it)
                }
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                insertDemoData()
            }
        }
    }
}

@Dao
interface MoneyInOutDao {
    @Insert
    fun insert(vararg moneyInOut: MoneyInOut)

    @Update
    fun update(vararg moneyInOut: MoneyInOut)

    @Delete
    fun delete(vararg moneyInOut: MoneyInOut)

    @Query("DELETE FROM moneyInOut")
    fun deleteAll()

    @Query("SELECT * FROM moneyInOut WHERE isDeleted =:isDeleted ORDER BY id DESC")
    fun getList(isDeleted: Boolean = false): List<MoneyInOut>

    @Query("SELECT * FROM moneyInOut WHERE isDeleted =:isDeleted ORDER BY id DESC")
    fun getLiveData(isDeleted: Boolean = false): LiveData<List<MoneyInOut>>

    @Query("SELECT * FROM moneyInOut WHERE isDeleted =:isDeleted ORDER BY id DESC")
    fun getListDeleted(isDeleted: Boolean = true): List<MoneyInOut>

    @Query("SELECT * FROM moneyInOut WHERE isDeleted =:isDeleted ORDER BY id DESC")
    fun getLiveDataDeleted(isDeleted: Boolean = true): LiveData<List<MoneyInOut>>

    @Query("SELECT * FROM moneyInOut ORDER BY id DESC")
    fun getListAll(): List<MoneyInOut>

    @Query("SELECT * FROM moneyInOut ORDER BY id DESC")
    fun getLiveDataAll(): LiveData<List<MoneyInOut>>
}

object MoneyIODBHelper {
    fun insert(moneyInOut: MoneyInOut) {
        MoneyInOutDB.get.dao().insert(moneyInOut)
    }

    fun insert(moneyInOuts: List<MoneyInOut>) {
        moneyInOuts.forEach { MoneyInOutDB.get.dao().insert(it) }
    }

    fun update(moneyInOut: MoneyInOut) {
        MoneyInOutDB.get.dao().update(moneyInOut)
    }

    fun update(moneyInOuts: List<MoneyInOut>) {
        moneyInOuts.forEach { MoneyInOutDB.get.dao().update(it) }
    }

    fun delete(moneyInOut: MoneyInOut) {
        MoneyInOutDB.get.dao().delete(moneyInOut)
    }

    fun delete(moneyInOuts: List<MoneyInOut>) {
        moneyInOuts.forEach { MoneyInOutDB.get.dao().delete(it) }
    }

    fun deleteAll() {
        MoneyInOutDB.get.dao().deleteAll()
    }

    fun getList(): List<MoneyInOut> {
        return MoneyInOutDB.get.dao().getList()
    }

    fun getLiveData(): LiveData<List<MoneyInOut>> {
        return MoneyInOutDB.get.dao().getLiveData()
    }

    fun getListDeleted(): List<MoneyInOut> {
        return MoneyInOutDB.get.dao().getListDeleted()
    }

    fun getLiveDataDeleted(): LiveData<List<MoneyInOut>> {
        return MoneyInOutDB.get.dao().getLiveDataDeleted()
    }

    fun getListAll(): List<MoneyInOut> {
        return MoneyInOutDB.get.dao().getListAll()
    }

    fun getLiveDataAll(): LiveData<List<MoneyInOut>> {
        return MoneyInOutDB.get.dao().getLiveDataAll()
    }

}