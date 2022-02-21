package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogSortMoneyIoBinding
import promax.dohaumen.financeapp.models.FilterMoneyIO
import promax.dohaumen.financeapp.models.FilterMoneyIOAdapter

class DialogSortMoneyIO(context: Context) {
    val dialog = Dialog(context)
    val b = DialogSortMoneyIoBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_sort_money_io, null))
    val adapter = FilterMoneyIOAdapter()

    init {
        dialog.setContentView(b.root)
        b.recyclerView.adapter = adapter
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.mode = "sort"
        adapter.setList(FilterMoneyIO.getListItemSortDefault().toMutableList())
    }

    fun setOnclickItem(onClick: (filterMoneyIO: FilterMoneyIO) -> Unit): DialogSortMoneyIO {
        adapter.onClickItem = {
            adapter.unCheckAll()
            it.isChecked = true
            it.reverse = b.checboxReverse.isChecked
            adapter.notifyDataSetChanged()
            dialog.cancel()
            onClick(it)
        }
        return this
    }

    fun show(): DialogSortMoneyIO {
        dialog.show()
        return this
    }
}
