package promax.dohaumen.financeapp.models

import android.annotation.SuppressLint
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
import promax.dohaumen.financeapp.databinding.ItemSortMoneyIoBinding
import promax.dohaumen.financeapp.dialogs.DialogConfirm
import promax.dohaumen.financeapp.helper.getColor
import promax.dohaumen.financeapp.helper.getCurrentDatetime
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.helper.removeAccents
import promax.dohaumen.financeapp.models.MoneyInOut.MoneyInOutType
import java.lang.Exception
import java.util.*

@Entity
class FilterMoneyIO: BaseModel {
    var filed = "" // field of MoneyIO
    var operator: String = ""
    var value: String = ""
    var type = "search" // search, filter, sort
    var defaultFilter = ""
    var isDefault = false // field to delete, insert Default filter in dialog

    @Ignore
    var isChecked = false
    @Ignore
    var reverse = false // for sort

    constructor()

    @Ignore
    constructor(name: String, field: String ="", type: String="",
                operator: String = "", value: String = "", isChecked: Boolean = false,
                defaultFilter: String = "",
                isDefault: Boolean = false
    ) {
        this.name = name
        this.filed = field
        this.operator = operator
        this.value = value
        this.type = type
        this.isChecked = isChecked
        this.defaultFilter = defaultFilter
        this.isDefault = isDefault
    }

    override fun equals(other: Any?): Boolean {
        if (other is FilterMoneyIO) {
            return  name == other.name &&
                    filed == other.filed &&
                    operator == other.operator &&
                    value == other.value &&
                    type == other.type
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return 31 * name.hashCode() +
                filed.hashCode() +
                operator.hashCode() +
                value.hashCode() +
                type.hashCode()
    }


    companion object {

        fun getFilterToday() =
            FilterMoneyIO(getStr(R.string.today), defaultFilter = "filter_today", isDefault = true, type = "filter")
        fun getFilterThisWeek() =
            FilterMoneyIO(getStr(R.string.this_week), defaultFilter = "filter_this_week", isDefault = true, type = "filter")
        fun getFilterThisMonth() =
            FilterMoneyIO(getStr(R.string.this_month), defaultFilter = "filter_this_month", isDefault = true, type = "filter")
        fun getFilterThisYear() =
            FilterMoneyIO(getStr(R.string.this_year), defaultFilter = "filter_this_year", isDefault = true, type = "filter")
        fun getFilterYesterday() =
            FilterMoneyIO(getStr(R.string.yesterday), defaultFilter = "filter_yesterday", isDefault = true, type = "filter")
        fun getFilterLastWeek() =
            FilterMoneyIO(getStr(R.string.last_week), defaultFilter = "filter_last_week", isDefault = true, type = "filter")
        fun getFilterLastMonth() =
            FilterMoneyIO(getStr(R.string.last_month), defaultFilter = "filter_last_month", isDefault = true, type = "filter")
        fun getFilterLastYear() =
            FilterMoneyIO(getStr(R.string.last_year), defaultFilter = "filter_last_year", isDefault = true, type = "filter")

        // not in db
        fun getListItemSearchDefault() = listOf(
            FilterMoneyIO(getStr(R.string.search_title_name), defaultFilter = "search_name", type = "search"),
            FilterMoneyIO(getStr(R.string.search_title_amount), defaultFilter = "search_amount", type = "search"),
            FilterMoneyIO(getStr(R.string.search_title_type), defaultFilter = "search_listType", type = "search"),
            FilterMoneyIO(getStr(R.string.search_title_time), defaultFilter = "search_datetime", type = "search"),
            FilterMoneyIO(getStr(R.string.search_title_description), defaultFilter = "search_desc", type = "search"),
        )

        fun getItemSortByDatetime() = FilterMoneyIO(getStr(R.string.date1),"datetime", "sort")

        // not in db
        fun getListItemSortDefault(): List<FilterMoneyIO> = listOf(
            FilterMoneyIO(getStr(R.string.create_time),"id","sort"),
            FilterMoneyIO(getStr(R.string.name1),"name", "sort"),
            FilterMoneyIO(getStr(R.string.amount2),"amount", "sort"),
            FilterMoneyIO(getStr(R.string.date1),"datetime", "sort", isChecked = true),
        )

        // in db
        fun getListItemFilterDefault(): List<FilterMoneyIO> = listOf(
            getFilterToday(),
            getFilterThisWeek(),
            getFilterThisMonth(),
            getFilterThisYear(),
            getFilterYesterday(),
            getFilterLastWeek(),
            getFilterLastMonth(),
            getFilterLastYear(),
            FilterMoneyIO(
                getStr(R.string.money_type__money_in),
                "type",
                "filter",
                operator = "=",
                value = MoneyInOutType.IN.toString(),
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.money_type__money_out),
                "type",
                "filter",
                operator = "=",
                value = MoneyInOutType.OUT.toString(),
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.currency__cash),
                "currency",
                "filter",
                operator = "=",
                value = Currency.CASH,
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.currency__bank),
                "currency",
                "filter",
                operator = "=",
                value = Currency.BANK,
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.record_has_time),
                "datetime",
                "filter",
                operator = "!=",
                value = "",
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.record_without_time),
                "datetime",
                "filter",
                operator = "=",
                value = "",
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.record_has_description),
                "desc",
                "filter",
                "!=",
                "",
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.record_without_description),
                "desc",
                "filter",
                "=",
                "",
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.amount_greater_than_or_equal_500000),
                "amount",
                "filter",
                ">=",
                "500000",
                isDefault = true
            ),
            FilterMoneyIO(
                getStr(R.string.filter_title_calculate_into_the_total_money_checked),
                defaultFilter = "calculate_into_the_total_money_checked",
                isDefault = true,
                type = "filter"
            ),
            FilterMoneyIO(
                getStr(R.string.filter_title_calculate_into_the_total_money_not_check),
                defaultFilter = "calculate_into_the_total_money_not_check",
                isDefault = true,
                type = "filter"
            ),
        )

        val operatorAvailable = mapOf(
            ">" to ">",
            "<" to ">",
            "=" to "=",
            ">=" to ">=",
            "<=" to "<=",
            getStr(R.string.not_equal) to "!=",
            getStr(R.string.contains) to "contains",
            getStr(R.string.not_contains) to "not contains"
        )
    }

    fun searchMoneyIO(list: List<MoneyInOut>): List<MoneyInOut> {
        var result = list
        val value = this.value.toLowerCase(Locale.ROOT)
        when (this.defaultFilter) {
            "search_name" -> {
                result = result.filter { it.name.toLowerCase(Locale.ROOT).removeAccents().contains(value.removeAccents()) }
            }
            "search_amount" -> {
                result = result.filter { it.amount == this.value }
            }
            "search_listType" -> {
                result = result.filter { it.listTypeOfSpending.toListString().contains(value) }
            }
            "search_datetime" -> {
                result = result.filter { it.datetime.toLowerCase(Locale.ROOT).contains(value) }
            }
            "search_desc" -> {
                result = result.filter { it.desc.toLowerCase(Locale.ROOT).removeAccents().contains(value.removeAccents()) }
            }
        }
        return result
    }

    fun sortMoneyIO(list: List<MoneyInOut>): List<MoneyInOut> {
        var result = list
        when (this.filed) {
            "name" -> result = result.sortedWith(compareBy({it.name}, {it.name}))
            "amount" -> {
                try {
                    result = result.sortedWith(compareBy({it.amount.toDouble()}, {it.amount.toDouble()})).reversed()
                } catch (e: Exception) {
                    return result
                }
            }
            "datetime" -> result = result.sortedWith(compareBy({it.datetime}, {it.datetime}))
            "id" -> result = result.sortedWith(compareBy({it.id}, {it.id})).reversed()
        }
        if (this.reverse) result = result.reversed()
        return result
    }


    fun filterMoneyIO(list: List<MoneyInOut>): List<MoneyInOut> {
        var result = list

        if (defaultFilter != "") {
            when(defaultFilter) {
                "filter_today" -> {
                    result = result.filter { it.datetime.isNotEmpty() && it.getDateTime().day == getCurrentDatetime().day }
                }
                "filter_this_week" -> {
                    result = result.filter {
                        it.datetime.isNotEmpty() && it.getDateTime().getWeekOfYear() == getCurrentDatetime().getWeekOfYear()
                    }
                }
                "filter_this_month" -> {
                    result = result.filter { it.datetime.isNotEmpty() && it.getDateTime().month == getCurrentDatetime().month }
                }
                "filter_this_year" -> {
                    result = result.filter { it.datetime.isNotEmpty() && it.getDateTime().year == getCurrentDatetime().year }
                }
                "filter_yesterday" -> {
                    result = result.filter { it.datetime.isNotEmpty() && it.getDateTime().equalsYMD(getCurrentDatetime().getYesterday()) }
                }
                "filter_last_week" -> {
                    result = result.filter {
                        it.datetime.isNotEmpty() &&
                        it.getDateTime().getWeekOfYear() == getCurrentDatetime().getLastWeek().getWeekOfYear()
                    }
                }
                "filter_last_month" -> {
                    result = result.filter { it.datetime.isNotEmpty() && it.getDateTime().equalsYM(getCurrentDatetime().getLastMonth()) }
                }
                "filter_last_year" -> {
                    result = result.filter { it.datetime.isNotEmpty() && it.getDateTime().year == getCurrentDatetime().year -1 }
                }
                "calculate_into_the_total_money_checked" -> {
                    result = result.filter { it.computeIntoTheTotalMoney }
                }
                "calculate_into_the_total_money_not_check" -> {
                    result = result.filter { !it.computeIntoTheTotalMoney }
                }
            }
        }

        // filed number
        if (filed in listOf("amount")) {
            when (this.operator) {
                ">" -> result = result.filter { it.getValueOfFieldNumber(filed) > value.toDouble() }
                "<" -> result = result.filter { it.getValueOfFieldNumber(filed) < value.toDouble() }
                "=" -> result = result.filter { it.getValueOfFieldString(filed) == value }
                ">=" -> result = result.filter { it.getValueOfFieldNumber(filed) >= value.toDouble() }
                "<=" -> result = result.filter { it.getValueOfFieldNumber(filed) <= value.toDouble() }
                "!=" -> result = result.filter { it.getValueOfFieldString(filed) != value }
                "contains" -> result = result.filter { it.getValueOfFieldString(this.filed).contains(value) }
                "not contains" -> result = result.filter { !it.getValueOfFieldString(this.filed).contains(value) }
            }
        } else {
            // field String
            when (this.operator) {
                ">" -> result = result.filter { it.getValueOfFieldString(this.filed) > value }
                "<" -> result = result.filter { it.getValueOfFieldString(this.filed) < value }
                "=" -> result = result.filter {
                    it.getValueOfFieldString(this.filed).toLowerCase(Locale.ROOT).removeAccents() ==
                            value.toLowerCase(Locale.ROOT).removeAccents()
                }
                ">=" -> result = result.filter { it.getValueOfFieldString(this.filed) >= value}
                "<=" -> result = result.filter { it.getValueOfFieldString(this.filed) <= value}
                "!=" -> result = result.filter { it.getValueOfFieldString(this.filed) != value}
                "contains" -> result = result.filter { it.getValueOfFieldString(this.filed).contains(value)}
                "not contains" -> result = result.filter { !it.getValueOfFieldString(this.filed).contains(value)}
            }
        }

        return result
    }

}

@Database(entities = [FilterMoneyIO::class], version = 3)
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

        fun insertListItemFilterDefault() {
            CoroutineScope(Dispatchers.IO).launch {
                FilterMoneyIO.getListItemFilterDefault().forEach { get.dao().insert(it) }
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                insertListItemFilterDefault()
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

    @Query("SELECT * FROM filtermoneyio WHERE type = :type")
    fun getListItemFilter(type: String = "filter"): List<FilterMoneyIO>

    @Query("SELECT * FROM filtermoneyio WHERE type = :type")
    fun getLiveDataItemFilter(type: String = "filter"): LiveData<List<FilterMoneyIO>>

    @Query("DELETE FROM filtermoneyio WHERE isDefault = :isDefault AND type = :type")
    fun deleteAllFilterDefault(isDefault: Boolean = true, type: String="filter")
}


class FilterMoneyIOAdapter : RecyclerView.Adapter<FilterMoneyIOAdapter.FilterMoneyIOHolder>() {

    private var list: MutableList<FilterMoneyIO> = mutableListOf()
    private lateinit var context: Context
    var mode = "small" // "small", "big", "sort"
    var hideImgDelete = false

    var onClickItem: (filterMoneyIO: FilterMoneyIO) -> Unit = {}
    var onClickImgDelete: (FilterMoneyIO: FilterMoneyIO) -> Unit = {}

    fun setList(list: MutableList<FilterMoneyIO>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun unCheckAll() {
        list.forEach { it.isChecked = false }
        notifyDataSetChanged()
    }

    fun getList() = list


    inner class FilterMoneyIOHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b: Any
        init {
            when (mode) {
                "small" -> {
                    b = ItemFilterMoneyIoSmallBinding.bind(itemView)
                    b.imgDelete.setOnClickListener {
                        val item = list[layoutPosition]
                        list.remove(item)
                        notifyDataSetChanged()
                        onClickImgDelete(item)
                    }
                }
                "big" -> {
                    b = ItemFilterMoneyIoBigBinding.bind(itemView)
                    val b1 = b as ItemFilterMoneyIoBigBinding
                    if (hideImgDelete) {
                        b.imgDelete.visibility = View.GONE
                    }

                    b1.imgDelete.setOnClickListener {
                        val item = list[layoutPosition]
                        DialogConfirm.show(context, message = "${getStr(R.string.delete)}: ${item.name}?") {
                            FilterMoneyIODB.get.dao().delete(item)
                        }
                    }
                    b1.bgItem.setOnClickListener {
                        onClickItem(list[layoutPosition])
                    }
                }
                else -> {
                    // mode = "sort"
                    b = ItemSortMoneyIoBinding.bind(itemView)
                    val b1 = b as ItemSortMoneyIoBinding
                    b1.bgItem.setOnClickListener {
                        onClickItem(list[layoutPosition])
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMoneyIOHolder {
        this.context = parent.context
        if (mode == "small") {
            val view = LayoutInflater.from(context).inflate(R.layout.item_filter_money_io_small, parent, false)
            return FilterMoneyIOHolder(view)
        } else if (mode == "big") {
            val view = LayoutInflater.from(context).inflate(R.layout.item_filter_money_io_big, parent, false)
            return FilterMoneyIOHolder(view)
        }
        // mode = sort
        val view = LayoutInflater.from(context).inflate(R.layout.item_sort_money_io, parent, false)
        return FilterMoneyIOHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilterMoneyIOHolder, position: Int) {
        val filterMoneyIO = list[position]
        when (mode) {
            "small" -> {
                val b = holder.b as ItemFilterMoneyIoSmallBinding
                b.tvName.text = filterMoneyIO.name
                when (filterMoneyIO.type) {
                    "search" -> b.imgThumbnail.setImageResource(R.drawable.ic_search)
                    "filter" -> b.imgThumbnail.setImageResource(R.drawable.ic_filter)
                }
            }
            "big" -> {
                val b = holder.b as ItemFilterMoneyIoBigBinding
                b.tvName.text = filterMoneyIO.name
                if (filterMoneyIO.isChecked) {
                    b.bgItem.setBackgroundColor(getColor(R.color.color_bg_item_sort_checked))
                } else {
                    b.bgItem.setBackgroundResource(R.drawable.rippler_white_blue)
                }
            }
            else -> {
                // sort
                val b = holder.b as ItemSortMoneyIoBinding
                b.tvName.text = "${getStr(R.string.sort_by)} ${filterMoneyIO.name}"
                if (filterMoneyIO.isChecked) {
                    b.bgItem.setBackgroundColor(getColor(R.color.color_bg_item_sort_checked))
                } else {
                    b.bgItem.setBackgroundResource(R.drawable.rippler_white_blue)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

}
