package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogFilterMoneyIoBinding
import promax.dohaumen.financeapp.helper.getKey
import promax.dohaumen.financeapp.models.FilterMoneyIO
import promax.dohaumen.financeapp.models.FilterMoneyIOAdapter
import promax.dohaumen.financeapp.models.FilterMoneyIODB


@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class DialogFilterMoneyIO(context: Context) {
    val dialog = Dialog(context)
    val b = DialogFilterMoneyIoBinding.bind(
        LayoutInflater.from(context).inflate(
            R.layout.dialog_filter_money_io,
            null
        )
    )
    val adapter = FilterMoneyIOAdapter()

    init {
        dialog.setContentView(b.root)
        b.recyclerView.adapter = adapter
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.mode = "big"
        FilterMoneyIODB.get.dao().getLiveDataItemFilter().observeForever {
            adapter.setList(it.toMutableList())
        }

        // todo: export string
        val fieldMap = mapOf<String, String>(
            "Name" to "name",
            "Amount" to "amount",
            "Currency" to "currency",
            "Money Type" to "type",
//            "Type" to "listTypeOfSpending",
            "Description" to "desc",
            "Date" to "datetime",
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
            val itemToInsert = FilterMoneyIO.getListItemFilter().toMutableList() subtract adapter.getList()
            itemToInsert.forEach {
                FilterMoneyIODB.get.dao().insert(it)
            }
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
