package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.icu.number.Precision.currency
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogFilterMoneyIoBinding
import promax.dohaumen.financeapp.helper.getKey
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.models.FilterMoneyIO
import promax.dohaumen.financeapp.models.FilterMoneyIOAdapter
import promax.dohaumen.financeapp.models.FilterMoneyIODB


@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class DialogFilterMoneyIO(context: Context) {
    val dialog = Dialog(context)
    val b = DialogFilterMoneyIoBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_filter_money_io, null))
    val adapter = FilterMoneyIOAdapter()

    fun getList() = adapter.getList()

    fun unCheck(filterMoneyIO: FilterMoneyIO) {
        adapter.getList().forEach {
            if (it == filterMoneyIO) {
                it.isChecked = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    init {
        dialog.setContentView(b.root)
        b.recyclerView.adapter = adapter
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.mode = "big"
        FilterMoneyIODB.get.dao().getLiveDataItemFilter().observeForever {
            adapter.setList(it.toMutableList())
        }

        val fieldMap = mapOf<String, String>(
            getStr(R.string.name2) to "name",
            getStr(R.string.amount) to "amount",
            getStr(R.string.currency) to "currency",
            getStr(R.string.money_type) to "type",
//            "Type" to "listTypeOfSpending",
            getStr(R.string.description) to "desc",
            getStr(R.string.date2) to "datetime",
//            "Calculate into the total money" to "computeIntoTheTotalMoney",
        )
        val fieldList = fieldMap.keys.toList()
        val operatorMap = FilterMoneyIO.operatorAvailable
        val operatorList = operatorMap.keys.toList()

        var field = ""
        var operator = ""


        b.spinnerFieldName.adapter =
            ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, fieldList)
        b.spinnerFieldName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).textSize = 13f
                field = fieldMap[fieldList[position]]!!
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        b.spinnerOperator.adapter =
            ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, operatorList)
        b.spinnerOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).textSize = 13f
                operator = operatorMap[operatorList[position]]!!
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        b.btnAddNewFilter.setOnClickListener {
            val value = b.editValue.text.toString()
            val filterMoneyIO = FilterMoneyIO(
                "${fieldMap.getKey(field)} ${operatorMap.getKey(operator)} $value", field, "filter", operator, value
            )
            FilterMoneyIODB.get.dao().insert(filterMoneyIO)
            Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
        }
        b.btnInsertDefault.setOnClickListener {
            FilterMoneyIODB.get.dao().deleteAllFilterDefault()
            FilterMoneyIODB.insertListItemFilterDefault()
        }
    }

    fun setOnclickItem(onClick: (filterMoneyIO: FilterMoneyIO) -> Unit): DialogFilterMoneyIO {
        adapter.onClickItem = {
            it.isChecked = !it.isChecked
            adapter.notifyDataSetChanged()
            dialog.cancel()
            onClick(it)
        }
        return this
    }

    fun show(): DialogFilterMoneyIO {
        dialog.show()
        return this
    }
}
