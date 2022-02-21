package promax.dohaumen.financeapp.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogShowMoneyHistoryBinding
import promax.dohaumen.financeapp.databinding.ItemMoneyHistoryBinding
import promax.dohaumen.financeapp.databinding.LayoutAddMoneyIoBinding
import promax.dohaumen.financeapp.dialogs.DialogAddMoneyIO


@Entity
class TotalMoneyInBanksHistory : BaseModel {
    constructor()

    @Ignore
    constructor(name: String) {
        this.name = name
    }
}

@Database(entities = [TotalMoneyInBanksHistory::class], version = 1)
abstract class TotalMoneyInBanksHistoryDB : RoomDatabase() {
    abstract fun dao(): TotalMoneyInBanksHistoryDao

    companion object {
        val get: TotalMoneyInBanksHistoryDB by lazy {
            synchronized(this) {
                Room.databaseBuilder(MyApp.context, TotalMoneyInBanksHistoryDB::class.java, "total_money_in_banks_history")
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}

@Dao
interface TotalMoneyInBanksHistoryDao {
    @Insert
    fun insert(vararg history: TotalMoneyInBanksHistory)

    @Update
    fun update(vararg history: TotalMoneyInBanksHistory)

    @Delete
    fun delete(vararg history: TotalMoneyInBanksHistory)

    @Query("DELETE FROM totalmoneyinbankshistory")
    fun deleteAll()

    @Query("SELECT * FROM totalmoneyinbankshistory ORDER BY id DESC")
    fun getList(): List<TotalMoneyInBanksHistory>

    @Query("SELECT * FROM totalmoneyinbankshistory ORDER BY id DESC")
    fun getLiveData(): LiveData<List<TotalMoneyInBanksHistory>>
}

class TotalMoneyInBanksHistoryAdpater:
    RecyclerView.Adapter<TotalMoneyInBanksHistoryAdpater.TotalMoneyInBankHistoryHolder>() {

    private lateinit var context: Context
    private var list: List<TotalMoneyInBanksHistory> = listOf()

    fun setList(list: List<TotalMoneyInBanksHistory>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TotalMoneyInBankHistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_money_history, parent, false)
        context = parent.context
        return TotalMoneyInBankHistoryHolder(view)
    }

    override fun onBindViewHolder(holder: TotalMoneyInBankHistoryHolder, position: Int) {
        val totalMoneyInBanksHistory = list[position]
        holder.b.tvMoney.text = totalMoneyInBanksHistory.name
        holder.b.tvDatetime.text = totalMoneyInBanksHistory.createTime
    }

    override fun getItemCount(): Int = list.size

    inner class TotalMoneyInBankHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ItemMoneyHistoryBinding.bind(itemView)
    }
}
