package promax.dohaumen.financeapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import promax.dohaumen.financeapp.MainActivity
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.adapters.MoneyInOutAdapter
import promax.dohaumen.financeapp.databinding.FragmentHomeBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.db.MoneyInOutDB
import promax.dohaumen.financeapp.dialogs.DialogAddMoneyIO
import promax.dohaumen.financeapp.dialogs.DialogFilterMoneyIO
import promax.dohaumen.financeapp.dialogs.DialogSortMoneyIO
import promax.dohaumen.financeapp.dialogs.DialogViewMoneyIO
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.models.FilterMoneyIO
import promax.dohaumen.financeapp.models.FilterMoneyIOAdapter
import promax.dohaumen.financeapp.models.MoneyInOut
import java.math.BigDecimal

@SuppressLint("SetTextI18n")
class HomeFragment: Fragment() {
    private lateinit var b: FragmentHomeBinding
    private val mainActivity: MainActivity by lazy { activity as MainActivity }
    private val moneyInOutAdapter = MoneyInOutAdapter()
    private val filterMoneyIOAdapter = FilterMoneyIOAdapter()

    private var listMoneyInOutLiveData = MutableLiveData(MoneyInOutDB.get.dao().getList().toMutableList())
    // current list in screen
    private var currentListMoneyIo = mutableListOf<MoneyInOut>()
    private var currentSort: FilterMoneyIO? = null
    private var currentSetFilter: Set<FilterMoneyIO> = mutableSetOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentHomeBinding.inflate(inflater, container, false)
        loadDataMoneyToTextView()
        setUpRecycleView()
        setUpLayoutFilterGroup()
        return b.root
    }

    private fun setUpRecycleView() {
        b.recyclerView.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerView.adapter = moneyInOutAdapter
        listMoneyInOutLiveData.observeForever {
            pagingForMoneyIO(it)
            loadDataTotalMoneyIOToText(it)
//            loadDataTotalMoneyIOToText()
        }
        setClickItemMoneyIO()
        setClickActionMoneyIO()
        setClickBtnAddMoneyIO()
    }

    private fun setUpLayoutFilterGroup() {
        // for search, filter
        b.recyclerViewFilterMoneyIo.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerViewFilterMoneyIo.adapter = filterMoneyIOAdapter
        filterMoneyIOAdapter.mode = "small"
        filterMoneyIOAdapter.onClickImgDelete = {
            // todo:
        }


        val dialogFilterMoneyIO = DialogFilterMoneyIO(mainActivity)
            .setOnclickItem {
                // todo:
            }
        b.imgFilter.setOnClickListener {
            dialogFilterMoneyIO.show()
        }
        b.imgSearch.setOnClickListener {

        }
        val dialogSortMoneyIO = DialogSortMoneyIO(mainActivity)
            .setOnclickItem { filterMoneyIO ->
                currentSort = filterMoneyIO
                pagingForMoneyIO()
            }
        b.imgSort.setOnClickListener {
           dialogSortMoneyIO.show()
        }
        b.imgReport.setOnClickListener {

        }
    }

    /**
     * @param _list: list to show in screen
     */
    private fun pagingForMoneyIO(_list: List<MoneyInOut> = currentListMoneyIo) {
        var list = _list
        val maxRecordsShowed = 100
        var start = 0
        var end = if (maxRecordsShowed > list.size) list.size else maxRecordsShowed

        currentSort?.let {
            list = it.sortMoneyIO(list)
        }

        fun initValue(_list: List<MoneyInOut>) {
            b.tvRecordsCount.text = _list.size.toString()
            b.tvRecordsCurrent.text = "${start+1}-$end / "
            if (_list.size < maxRecordsShowed) {
                end = _list.size
            }
            currentListMoneyIo = _list.subList(start, end).toMutableList()
            moneyInOutAdapter.setList(currentListMoneyIo)
        }
        initValue(list)


        b.imgRight.setOnClickListener {
            start += maxRecordsShowed
            end += maxRecordsShowed
            if (start >= list.size) {
                start = 0
                end = maxRecordsShowed
            }
            if (end >= list.size) {
                end = list.size
            }

            currentListMoneyIo = list.subList(start, end).toMutableList()
            moneyInOutAdapter.setList(currentListMoneyIo)
//            adapter.setStartIndex(start)
            b.tvRecordsCurrent.text = "${start+1}-$end / "
//            b.tvRecordsCount.text = records.size.toString()

        }
        b.imgLeft.setOnClickListener {
            start -= maxRecordsShowed
            end -= maxRecordsShowed
            if (start < 0) {
                start = list.size - maxRecordsShowed
                end = list.size
                if (start < 0) {
                    start = 0
                }
            }

            currentListMoneyIo = list.subList(start, end).toMutableList()
            moneyInOutAdapter.setList(currentListMoneyIo)
//            adapter.setStartIndex(start)
            b.tvRecordsCurrent.text = "${start+1}-$end / "
//            b.tvRecordsCount.text = records.size.toString()
        }

    }

    fun notifyMoneyUnitOrMoneyFormatChanged() {
//        loadDataTotalMoneyIOToText()
        moneyInOutAdapter.notifyDataSetChanged()
    }

    private fun loadDataMoneyToTextView() {
        AppData.getTotalMoneyFormatedLiveData().observeForever {
            b.tvAmountAvailableValue.text = it.toString()
        }
        AppData.getTotalMoneyInBanksFormatedLiveData().observeForever {
            b.tvTotalMoneyInBanksValue.text = it.toString()
        }
        AppData.getTotalCashFormatedLiveData().observeForever {
            b.tvTotalCashValue.text = it
        }
    }

    private fun loadDataTotalMoneyIOToText(list: List<MoneyInOut>) {
        var totalMoneyIn = BigDecimal("0")
        var totalMoneyOut = BigDecimal("0")

        list.filter { it.type == MoneyInOut.MoneyInOutType.IN }.forEach {
            totalMoneyIn += BigDecimal(it.amount)
        }
        list.filter { it.type == MoneyInOut.MoneyInOutType.OUT }.forEach {
            totalMoneyOut += BigDecimal(it.amount)
        }
        b.tvTotalMoneyIn.text = "${getStr(R.string.total_money_in)} ${AppData.formatMoneyWithAppConfig(totalMoneyIn.toPlainString())}"
        b.tvTotalMoneyOut.text = "${getStr(R.string.total_money_out)} ${AppData.formatMoneyWithAppConfig(totalMoneyOut.toPlainString())}"
    }

    private fun setClickItemMoneyIO() {
        moneyInOutAdapter.onClickItem = { moneyIO ->
            DialogViewMoneyIO.show(b.root, moneyIO) {
                listMoneyInOutLiveData.value = MoneyInOutDB.get.dao().getList().toMutableList()
            }
        }
        moneyInOutAdapter.onLongClickItem = { moneyIO1 ->
            // show layout action, hide navbottom, hide floating button add money io
            b.layoutActionMoneyIo.root.visibility = View.VISIBLE
            b.btnAddMoneyIo.visibility = View.GONE
            mainActivity.b.bottomNav.visibility = View.GONE


            moneyInOutAdapter.setSwapCheckItem(moneyIO1)
            moneyInOutAdapter.onClickItem = { moneyIO2 ->
                moneyInOutAdapter.setSwapCheckItem(moneyIO2)
            }
        }

    }

    private fun setClickActionMoneyIO() {
        fun cancelAction() {
            b.layoutActionMoneyIo.root.visibility = View.GONE
            b.btnAddMoneyIo.visibility = View.VISIBLE
            mainActivity.b.bottomNav.visibility = View.VISIBLE

            setClickItemMoneyIO()
            moneyInOutAdapter.unCheckAll()
        }

        b.layoutActionMoneyIo.actionSelectAll.setOnClickListener { moneyInOutAdapter.setCheckAll() }
        b.layoutActionMoneyIo.actionCancel.setOnClickListener { cancelAction() }
        b.layoutActionMoneyIo.actionDelete.setOnClickListener {
            val itemsToDelete = moneyInOutAdapter.getListChecked()
            if (itemsToDelete.isEmpty()) {
                Toast.makeText(mainActivity, getString(R.string.you_havennot_selected_one_yet), Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.delete))
                    .setMessage("${getString(R.string.are_you_sure_to_delete)} ${itemsToDelete.size} ${getString(R.string.records)}")
                    .setPositiveButton(getString(R.string.delete)) {_1, _2 ->
                        itemsToDelete.forEach {
                            it.isDeleted = true
                            MoneyInOutDB.get.dao().update(it)
                            listMoneyInOutLiveData.value = MoneyInOutDB.get.dao().getList().toMutableList()
                            AppData.refundTheAmount(it)
                        }
                        Snackbar.make(b.recyclerView, getString(R.string.deleted),2000)
                            .setAction(R.string.undo) {
                                itemsToDelete.forEach {
                                    it.isDeleted = false
                                    MoneyInOutDB.get.dao().update(it)
                                    listMoneyInOutLiveData.value = MoneyInOutDB.get.dao().getList().toMutableList()
                                    AppData.calculateIntoTheAmount(it)
                                }
                            }.show()
                        cancelAction()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _1, _2 -> }.show()
            }
        }
    }


    private fun setClickBtnAddMoneyIO() {
        b.btnAddMoneyIo.setOnClickListener {
            DialogAddMoneyIO.setBgAlpha(0.6f).show(mainActivity.b.bgMainActivity) {
                listMoneyInOutLiveData.value = MoneyInOutDB.get.dao().getList().toMutableList()
            }
        }
    }

}
