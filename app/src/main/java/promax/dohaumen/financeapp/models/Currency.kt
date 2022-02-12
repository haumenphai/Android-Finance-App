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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.ItemCurrecyBinding

@Entity
// currecy: bank, cash, ...
class Currency {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var imgRes: Int = -1

    @Ignore
    var isChecked: Boolean = false

    constructor()

    @Ignore
    constructor(name: String) {
        this.name = name
    }

    companion object {
        fun getDataDefault() = listOf(
            Currency(MyApp.context.getString(R.string.bank)),
            Currency(MyApp.context.getString(R.string.cash))
        )
    }
}


@Database(entities = [Currency::class], version = 1)
abstract class CurrencyDB : RoomDatabase() {
    abstract fun dao(): CurrencyDao

    companion object {
        val get: CurrencyDB by lazy {
            synchronized(this) {
                Room.databaseBuilder(MyApp.context, CurrencyDB::class.java, "currency_db")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun insertDemoData() {
            CoroutineScope(Dispatchers.IO).launch {
                Currency.getDataDefault().forEach { get.dao().insert(it) }
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
interface CurrencyDao {
    @Insert
    fun insert(vararg currency: Currency)

    @Update
    fun update(vararg currency: Currency)

    @Delete
    fun delete(vararg currency: Currency)

    @Query("DELETE FROM currency")
    fun deleteAll()

    @Query("SELECT * FROM currency ORDER BY id DESC")
    fun getList(): List<Currency>

    @Query("SELECT * FROM currency ORDER BY id DESC")
    fun getLiveData(): LiveData<List<Currency>>
}


class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>() {
    private var list: List<Currency> = mutableListOf()
    private lateinit var context: Context

    var onClickItem: (currency: Currency) -> Unit = {}
    var onLongClickItem: (currency: Currency) -> Unit = {}
    var hideImgDelete = false

    fun setList(list: List<Currency>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun getList() = list


    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ItemCurrecyBinding.bind(itemView)
        init {
            if (hideImgDelete) {
                b.imgDelete.visibility = View.GONE
            }

            b.bgItem.setOnClickListener {
                onClickItem(list[layoutPosition])
            }
            b.bgItem.setOnLongClickListener {
                onLongClickItem(list[layoutPosition])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currecy, parent, false)
        this.context = parent.context
        return CurrencyHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val currency = list[position]

        holder.b.tvSequence.text = "#${position+1}"
        holder.b.tvName.text = currency.name

        if (currency.imgRes != -1) {
            holder.b.imgThumbnail.setImageResource(currency.imgRes)
        }

        holder.b.imgDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.delete)
                .setMessage("${context.getString(R.string.delete)} ${currency.name}?")
                .setPositiveButton(R.string.delete) { i1,i2 ->
                    CurrencyDB.get.dao().delete(currency)
                }
                .setNegativeButton(R.string.cancel) {i1,i2 -> }
                .show()
        }

    }

    override fun getItemCount(): Int = list.size
}
