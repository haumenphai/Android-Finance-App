package promax.dohaumen.financeapp.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.ItemMoneyHistoryBinding

@Entity
class TotalCashHistory: BaseModel {
    constructor()

    @Ignore
    constructor(name: String) {
        this.name = name
    }
}

@Database(entities = [TotalCashHistory::class], version = 1)
abstract class TotalCashHistoryDB : RoomDatabase() {
    abstract fun dao(): TotalCashHistoryDao

    companion object {
        val get: TotalCashHistoryDB by lazy {
            synchronized(this) {
                Room.databaseBuilder(MyApp.context, TotalCashHistoryDB::class.java, "total_cash_history")
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}

@Dao
interface TotalCashHistoryDao {
    @Insert
    fun insert(vararg history: TotalCashHistory)

    @Update
    fun update(vararg history: TotalCashHistory)

    @Delete
    fun delete(vararg history: TotalCashHistory)

    @Query("DELETE FROM totalcashhistory")
    fun deleteAll()

    @Query("SELECT * FROM totalcashhistory ORDER BY id DESC")
    fun getList(): List<TotalCashHistory>

    @Query("SELECT * FROM totalcashhistory ORDER BY id DESC")
    fun getLiveData(): LiveData<List<TotalCashHistory>>
}

class TotalCashHistoryAdapter:
    RecyclerView.Adapter<TotalCashHistoryAdapter.TotalCashHistoryAdapterHolder>() {

    private lateinit var context: Context
    private var list: List<TotalCashHistory> = listOf()

    fun setList(list: List<TotalCashHistory>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TotalCashHistoryAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_money_history, parent, false)
        context = parent.context
        return TotalCashHistoryAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: TotalCashHistoryAdapterHolder, position: Int) {
        val history = list[position]
        holder.b.tvMoney.text = history.name
        holder.b.tvDatetime.text = history.createTime
    }

    override fun getItemCount(): Int = list.size

    inner class TotalCashHistoryAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ItemMoneyHistoryBinding.bind(itemView)
    }
}
