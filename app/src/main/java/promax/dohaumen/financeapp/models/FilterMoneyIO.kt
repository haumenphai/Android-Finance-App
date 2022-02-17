package promax.dohaumen.financeapp.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.ItemFilterMoneyIoBigBinding
import promax.dohaumen.financeapp.databinding.ItemFilterMoneyIoSmallBinding
import promax.dohaumen.financeapp.helper.getStr

@Entity
class FilterMoneyIO: BaseModel {
    var filed = "" // see MoneyInout.getSearchableFields()
    var operator: String = ""
    var value: String = ""
    var type = "search" // search or filter


    constructor()

    @Ignore
    constructor(field: String, operator: String, value: String, type: String) {
        this.filed = field
        this.operator = operator
        this.value = value
        this.type = type
    }

    companion object {
        val operatorAvailable = mapOf(
            ">" to ">",
            "<" to ">",
            "=" to "=",
            ">=" to ">=",
            "<=" to "<=",
            "!=" to getStr(R.string.not_equal),
            "contains" to getStr(R.string.contains),
            "not contains" to getStr(R.string.not_contains)
        )
    }

    enum class FilterMoneyIOType {SEARCH, FILTER}
}

@Database(entities = [FilterMoneyIO::class], version = 1)
abstract class FilterMoneyIODB : RoomDatabase() {
    abstract fun dao(): FilterMoneyIODao

    companion object {
        val get: FilterMoneyIODB by lazy {
            synchronized(this) {
                Room.databaseBuilder(MyApp.context, FilterMoneyIODB::class.java, "filter_money_io")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun insertDemoData() {
            CoroutineScope(Dispatchers.IO).launch {
//                FilterMoneyIO.getDataDefault().forEach { get.dao().insert(it) }
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
interface FilterMoneyIODao {
    @Insert
    fun insert(vararg filterMoneyIO: FilterMoneyIO)

    @Update
    fun update(vararg filterMoneyIO: FilterMoneyIO)

    @Delete
    fun delete(vararg filterMoneyIO: FilterMoneyIO)

    @Query("DELETE FROM filtermoneyio")
    fun deleteAll()

    @Query("SELECT * FROM filtermoneyio ORDER BY id DESC")
    fun getList(): List<FilterMoneyIO>

    @Query("SELECT * FROM filtermoneyio ORDER BY id DESC")
    fun getLiveData(): LiveData<List<FilterMoneyIO>>
}


class FilterMoneyIOAdapter : RecyclerView.Adapter<FilterMoneyIOAdapter.FilterMoneyIOHolder>() {

    private var list: List<FilterMoneyIO> = mutableListOf()
    private lateinit var context: Context
    var mode = "small" // "small" or "big"

    var onClickItem: (filterMoneyIO: FilterMoneyIO) -> Unit = {}
    var onClickImgDelete: (FilterMoneyIO: FilterMoneyIO) -> Unit = {}

    fun setList(list: List<FilterMoneyIO>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun getList() = list


    inner class FilterMoneyIOHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b: Any
        init {
            if (mode == "small") {
                b = ItemFilterMoneyIoSmallBinding.bind(itemView)
                b.imgDelete.setOnClickListener {
                    onClickImgDelete(list[layoutPosition])
                }
            } else {
                b = ItemFilterMoneyIoBigBinding.bind(itemView)
                val b1 = b as ItemFilterMoneyIoBigBinding
                b1.imgDelete.setOnClickListener {
                    FilterMoneyIODB.get.dao().delete(list[layoutPosition])
                }
                b1.bgItem.setOnClickListener {
                    onClickItem(list[layoutPosition])
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMoneyIOHolder {
        this.context = parent.context
        if (mode == "small") {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_money_io_small, parent, false)
            return FilterMoneyIOHolder(view)
        } else  {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_money_io_big, parent, false)
            return FilterMoneyIOHolder(view)
        }

    }


    override fun onBindViewHolder(holder: FilterMoneyIOHolder, position: Int) {
        val filterMoneyIO = list[position]
        if (mode == "small") {
            val b = holder.b as ItemFilterMoneyIoSmallBinding
            b.tvName.text = filterMoneyIO.name
            when (filterMoneyIO.type) {
                "search" -> b.imgThumbnail.setImageResource(R.drawable.ic_search)
                "filter" -> b.imgThumbnail.setImageResource(R.drawable.ic_filter)
            }
        } else {
            val b = holder.b as ItemFilterMoneyIoBigBinding
            b.tvName.text = filterMoneyIO.name
        }
    }

    override fun getItemCount(): Int = list.size

}
