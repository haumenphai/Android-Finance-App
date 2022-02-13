package promax.dohaumen.financeapp.models

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
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
class MoneyType: BaseModel {
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

fun List<MoneyType>.toJson() = Gson().toJson(this)

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

    var onClickItem: (moneyType: MoneyType) -> Unit = {}
    var onLongClickItem: (moneyType: MoneyType) -> Unit = {}

    var modeInDialog = false
    var hideIconDelete = false

    fun setList(list: MutableList<MoneyType>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun getList() = list

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
        } else {
            val b = holder.b as ItemMoneyTypeBinding

            b.tvSequence.text = "#${position+1}"
            b.tvName.text = moneyType.name

            if (moneyType.type == MoneyInOutType.IN) {
                b.bgItem.setBackgroundResource(R.drawable.ripple_item_money_in)
            } else {
                b.bgItem.setBackgroundResource(R.drawable.ripple_item_money_out)
            }

            if (hideIconDelete) {
                b.imgDelete.visibility = View.GONE
            }

            b.imgDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle(R.string.delete)
                    .setMessage("${context.getString(R.string.delete)} ${moneyType.name}?")
                    .setPositiveButton(R.string.delete) { i1,i2 ->
                        MoneyTypeDB.get.dao().delete(moneyType)
                    }
                    .setNegativeButton(R.string.cancel) {i1,i2 -> }
                    .show()
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
                    notifyDataSetChanged()
                }
                if (hideIconDelete) {
                    b1.imgDelete.visibility = View.GONE
                }
            } else {
                b = ItemMoneyTypeBinding.bind(itemView)
                val b1 = (b as ItemMoneyTypeBinding)

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
