package promax.dohaumen.financeapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import promax.dohaumen.financeapp.MainActivity
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.adapters.MoneyInOutAdapter
import promax.dohaumen.financeapp.databinding.FragmentHomeBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.db.MoneyInOutDB
import promax.dohaumen.financeapp.dialogs.*
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
    private var listMoneyIO = listOf<MoneyInOut>() // list in db

    // current list after filter,sort,search by
    private var currentListMoneyIo = listOf<MoneyInOut>()
    private var currentSort = FilterMoneyIO.getItemSortByDatetime().apply { this.reverse = true }
    private var currentSetFilter: MutableSet<FilterMoneyIO> = mutableSetOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentHomeBinding.inflate(inflater, container, false)
        loadDataMoneyToTextView()
        setUpRecycleView()
        setUpLayoutFilterGroup()
        return b.root
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

    private fun setUpRecycleView() {
        b.recyclerView.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerView.adapter = moneyInOutAdapter
        MoneyInOutDB.get.dao().getLiveData().observeForever {
            listMoneyIO = it
            pagingForMoneyIO(listMoneyIO)
            loadDataTotalMoneyIOToText(listMoneyIO)
        }
        setClickItemMoneyIO()
        setClickActionMoneyIO()
        setClickBtnAddMoneyIO()
    }

    private fun setUpLayoutFilterGroup() {
        val dialogFilterMoneyIO = DialogFilterMoneyIO(mainActivity)
            .setOnclickItem {
                if (it.isChecked) {
                    currentSetFilter.add(it)
                } else {
                    currentSetFilter.remove(it)
                }
                filterMoneyIOAdapter.setList(currentSetFilter.toMutableList())
                pagingForMoneyIO(listMoneyIO)
            }
        val dialogSortMoneyIO = DialogSortMoneyIO(mainActivity)
            .setOnclickItem { filterMoneyIO ->
                currentSort = filterMoneyIO
                pagingForMoneyIO()
            }
        val dialogSearchMoneyIO = DialogSearchMoneyIO(mainActivity)
            .setOnClickItem {
                currentSetFilter.add(it)
                filterMoneyIOAdapter.setList(currentSetFilter.toMutableList())
                pagingForMoneyIO(listMoneyIO)
            }

        val dialogLoading = DialogLoading(mainActivity)
        val dialogReportMoneyIO = DialogReportMoneyIO(mainActivity)
            .setOnPreProcessShowDetail {
                dialogLoading.show()
            }
            .setOnProcessShowDetailComplete {
                dialogLoading.cancel()
            }
        dialogLoading.setOnClickBtnCancel { dialogReportMoneyIO.cancelJob() }

        // for search, filter
        b.recyclerViewFilterMoneyIo.layoutManager = GridLayoutManager(mainActivity, 2)
        b.recyclerViewFilterMoneyIo.adapter = filterMoneyIOAdapter
        filterMoneyIOAdapter.apply {
            this.setList(currentSetFilter.toMutableList())
            this.mode = "small"
            this.onClickImgDelete = {
                currentSetFilter = this.getList().toMutableSet()
                pagingForMoneyIO(listMoneyIO)
                dialogFilterMoneyIO.unCheck(it)
            }
        }

        b.imgFilter.setOnClickListener {
            dialogFilterMoneyIO.show()
        }
        b.imgSearch.setOnClickListener {
            dialogSearchMoneyIO.show()
        }

        b.imgSort.setOnClickListener {
           dialogSortMoneyIO.show()
        }
        b.imgReport.setOnClickListener {
            dialogLoading.show()
            dialogReportMoneyIO.setListMoneyIO(currentListMoneyIo) {
                dialogReportMoneyIO.show()
                dialogLoading.cancel()
            }
        }
    }

    private var job1: Job? = null
    private fun pagingForMoneyIO(_list: List<MoneyInOut> = currentListMoneyIo) {
        job1?.cancel()
        job1 = GlobalScope.launch {
            // Step 1: Compute currentListMoneyIo by current sort, current filter and current search
            currentListMoneyIo = _list
            val maxRecordsShowed = 100
            var start = 0
            var end = if (maxRecordsShowed > currentListMoneyIo.size) currentListMoneyIo.size else maxRecordsShowed

            currentListMoneyIo = currentSort.sortMoneyIO(currentListMoneyIo)
            currentSetFilter.forEach {
                if (it.type == "search") {
                    currentListMoneyIo = it.searchMoneyIO(currentListMoneyIo)
                } else if (it.type == "filter") {
                    currentListMoneyIo = it.filterMoneyIO(currentListMoneyIo)
                }
            }

            // Step 2: Paging for moneyIO with currentListMoneyIo
            withContext(Dispatchers.Main) {
                loadDataTotalMoneyIOToText(currentListMoneyIo)

                fun initValue(_list1: List<MoneyInOut>) {
                    b.tvRecordsCount.text = _list1.size.toString()
                    b.tvRecordsCurrent.text = "${start+1}-$end / "
                    if (_list1.size < maxRecordsShowed) {
                        end = _list1.size
                    }
                    moneyInOutAdapter.setList(_list1.subList(start, end).toMutableList())
                }
                initValue(currentListMoneyIo)


                b.imgRight.setOnClickListener {
                    start += maxRecordsShowed
                    end += maxRecordsShowed
                    if (start >= currentListMoneyIo.size) {
                        start = 0
                        end = maxRecordsShowed
                    }
                    if (end >= currentListMoneyIo.size) {
                        end = currentListMoneyIo.size
                    }

                    moneyInOutAdapter.setList(currentListMoneyIo.subList(start, end).toMutableList())
                    moneyInOutAdapter.setStartIndex(start)
                    b.tvRecordsCurrent.text = "${start+1}-$end / "
                }
                b.imgLeft.setOnClickListener {
                    start -= maxRecordsShowed
                    end -= maxRecordsShowed
//                    if (start < 0) {
//                        start = currentListMoneyIo.size - maxRecordsShowed
//                        end = currentListMoneyIo.size
//                        if (start < 0) {
//                            start = 0
//                        }
//                    }
                    if (start < 0) {
                        start = 0
                        end = maxRecordsShowed
                    }
                    if (end >= currentListMoneyIo.size) {
                        end = currentListMoneyIo.size
                    }

                    moneyInOutAdapter.setList(currentListMoneyIo.subList(start, end).toMutableList())
                    moneyInOutAdapter.setStartIndex(start)
                    b.tvRecordsCurrent.text = "${start+1}-$end / "
                }
            }
        }
    }



    fun notifyMoneyUnitOrMoneyFormatChanged() {
        moneyInOutAdapter.notifyDataSetChanged()
        loadDataTotalMoneyIOToText(currentListMoneyIo)
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
            DialogViewMoneyIO.show(b.root, moneyIO)
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
                            AppData.refundTheAmount(it)
                        }
                        Snackbar.make(b.recyclerView, getString(R.string.deleted),2000)
                            .setAction(R.string.undo) {
                                itemsToDelete.forEach {
                                    it.isDeleted = false
                                    MoneyInOutDB.get.dao().update(it)
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
            DialogAddMoneyIO.setBgAlpha(0.6f).show(mainActivity.b.bgMainActivity)
        }
    }

}
