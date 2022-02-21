package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogSearchMoneyIoBinding
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.models.FilterMoneyIO
import promax.dohaumen.financeapp.models.FilterMoneyIOAdapter
import promax.dohaumen.financeapp.models.toListString
import java.util.*

class DialogSearchMoneyIO(context: Context) {
    val dialog = Dialog(context)
    val b = DialogSearchMoneyIoBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_search_money_io, null))
    val adapter = FilterMoneyIOAdapter()

    init {
        dialog.setContentView(b.root)
        b.recyclerViewSearch.layoutManager = LinearLayoutManager(context)
        b.recyclerViewSearch.adapter = adapter
        adapter.apply {
            this.mode = "big"
            this.hideImgDelete = true
            this.setList(FilterMoneyIO.getListItemSearchDefault().toMutableList())
        }
        b.editName.addTextChangedListener {
            val value = b.editName.text.toString()
            adapter.getList().forEach {
                it.value = value
                when (it.defaultFilter) {
                    "search_name" -> {
                        it.name = getStr(R.string.search_title_name) + value
                    }
                    "search_amount" -> {
                        it.name = getStr(R.string.search_title_amount) + value
                    }
                    "search_listType" -> {
                        it.name = getStr(R.string.search_title_type) + value
                    }
                    "search_datetime" -> {
                        it.name = getStr(R.string.search_title_time) + value
                    }
                    "search_desc" -> {
                        it.name = getStr(R.string.search_title_description) + value
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }

    }

    fun setOnClickItem(onClick:(item: FilterMoneyIO) -> Unit): DialogSearchMoneyIO {
        adapter.onClickItem = {
            onClick(it)
            dialog.cancel()
        }
        return this
    }

    fun show(): DialogSearchMoneyIO {
        dialog.show()
        return this
    }

}