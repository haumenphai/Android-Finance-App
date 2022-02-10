package promax.dohaumen.financeapp.models

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.ItemCurrecyBinding
import promax.dohaumen.financeapp.databinding.ItemMoneyTypeBinding
import promax.dohaumen.financeapp.databinding.ItemMoneyTypeInDialogCreateMoneyIoBinding
import promax.dohaumen.financeapp.models.MoneyInOut.MoneyInOutType


@Entity
// example: Subsistence (sinh hoạt phí), invest (đầu tư), sales money
class MoneyType {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var type: MoneyInOutType = MoneyInOutType.IN

    @Ignore
    var isChecked: Boolean = false

    constructor()

    @Ignore
    constructor(name: String, type: MoneyInOutType) {
        this.name = name
        this.type = type
    }

    companion object {
        fun getDataDefault() = listOf(
            MoneyType(MyApp.context.getString(R.string.sales_money), MoneyInOutType.IN),
            MoneyType(MyApp.context.getString(R.string.invest), MoneyInOutType.OUT)
        )
    }

}

@Database(entities = [MoneyType::class], version = 1)
abstract class MoneyTypeDB : RoomDatabase() {
    abstract fun dao(): MoneyTypeDao

    companion object {
        val get: MoneyTypeDB by lazy {
            synchronized(this) {
                Room.databaseBuilder(MyApp.context, MoneyTypeDB::class.java, "money_type_db")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun insertDemoData() {
            CoroutineScope(Dispatchers.IO).launch {
                MoneyType.getDataDefault().forEach { get.dao().insert(it) }
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
interface MoneyTypeDao {
    @Insert
    fun insert(vararg moneyType: MoneyType)

    @Update
    fun update(vararg moneyType: MoneyType)

    @Delete
    fun delete(vararg moneyType: MoneyType)

    @Query("DELETE FROM moneytype")
    fun deleteAll()

    @Query("SELECT * FROM moneytype ORDER BY id DESC")
    fun getList(): List<MoneyType>

    @Query("SELECT * FROM moneytype ORDER BY id DESC")
    fun getLiveData(): LiveData<List<MoneyType>>
}


class MoneyTypeAdapter: RecyclerView.Adapter<MoneyTypeAdapter.MoneyTypeHolder>() {
    private var list: MutableList<MoneyType> = mutableListOf()
    private lateinit var context: Context

    lateinit var onClickItem: (moneyType: MoneyType) -> Unit
    lateinit var onLongClickItem: (moneyType: MoneyType) -> Unit

    var modeInDialog = false
    var hideIconDelete = false

    fun setList(list: MutableList<MoneyType>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun getList() = list

    fun setCheckAll() {
        list.forEach { it.isChecked = true }
        notifyDataSetChanged()
    }

    fun unCheckAll() {
        list.forEach { it.isChecked = false }
        notifyDataSetChanged()
    }

    fun getListChecked() = list.filter { it.isChecked }

    fun setSwapCheckItem(moneyType: MoneyType) {
        moneyType.isChecked = !moneyType.isChecked
        notifyItemChanged(list.indexOf(moneyType))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyTypeHolder {
        this.context = parent.context
        if (modeInDialog) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_money_type_in_dialog_create_money_io, parent, false)
            return MoneyTypeHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currecy, parent, false)
            return MoneyTypeHolder(view)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MoneyTypeHolder, position: Int) {
        val moneyType = list[position]

        if (modeInDialog) {
            val b = holder.b as ItemMoneyTypeInDialogCreateMoneyIoBinding
            b.tvName.text = moneyType.name

            if (hideIconDelete) {
                b.imgDelete.visibility = View.GONE
            }

        } else {
            val b = holder.b as ItemMoneyTypeBinding

            b.tvSequence.text = "#${position+1}"
            b.tvName.text = moneyType.name
            if (moneyType.isChecked) {
                b.bgItem.setBackgroundColor(ContextCompat.getColor(context, R.color.red_500))
            } else {
                b.bgItem.setBackgroundResource(R.drawable.rippler_yellow)
            }
        }

    }
    override fun getItemCount(): Int = list.size


    inner class MoneyTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var b: Any

        init {
            if (modeInDialog) {
                b = ItemMoneyTypeInDialogCreateMoneyIoBinding.bind(itemView)
                val b1 = (b as ItemMoneyTypeInDialogCreateMoneyIoBinding)
                b1.imgDelete.setOnClickListener {
                    list.removeAt(layoutPosition)
                    notifyItemChanged(layoutPosition)
                }
            } else {
                b = ItemCurrecyBinding.bind(itemView)
                val b1 = (b as ItemCurrecyBinding)

                b1.bgItem.setOnClickListener {
                    onClickItem(list[layoutPosition])
                }
                b1.bgItem.setOnLongClickListener {
                    onLongClickItem(list[layoutPosition])
                    true
                }
            }

        }
    }
}
