package promax.dohaumen.financeapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import promax.dohaumen.financeapp.MainActivity
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.adapters.MoneyInOutAdapter
import promax.dohaumen.financeapp.databinding.FragmentHomeBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.db.MoneyInOutDB
import promax.dohaumen.financeapp.dialogs.DialogAddMoneyIO
import promax.dohaumen.financeapp.dialogs.DialogViewMoneyIO
import promax.dohaumen.financeapp.helper.formatNumber
import promax.dohaumen.financeapp.models.MoneyInOut

@SuppressLint("SetTextI18n")
class HomeFragment: Fragment() {
    private lateinit var b: FragmentHomeBinding
    private val mainActivity: MainActivity by lazy { activity as MainActivity }
    private val moneyInOutAdapter = MoneyInOutAdapter()

    // current list in screen
    private var listMoneyInOut = MoneyInOutDB.get.dao().getList().toMutableList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = FragmentHomeBinding.inflate(inflater, container, false)
        b.recyclerView.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerView.adapter = moneyInOutAdapter
        moneyInOutAdapter.setList(listMoneyInOut)
        loadDataTotalMoneyIOToText()

        loadDataMoneyToTextView()
        setClickItemMoneyIO()
        setClickActionMoneyIO()
        setClickBtnAddMoneyIO()
        return b.root
    }

    fun notifyMoneyUnitOrMoneyFormatChanged() {
        loadDataTotalMoneyIOToText()
        moneyInOutAdapter.notifyDataSetChanged()
    }

    private fun loadDataMoneyToTextView() {
        AppData.getTotalMoneyFormatedLiveData().observeForever {
            b.tvAmountAvailableValue.text = it.toString()
        }
        AppData.getTotalMoneyInBanksFormatedLiveData().observeForever {
            b.tvTotalMoneyInBanksValue.text = it.toString()
        }
        AppData.getTotalMoneyFormatedLiveData().observeForever {
            b.tvTotalCashValue.text = it
        }
    }

    private fun loadDataTotalMoneyIOToText() {
        val totalMoneyIn = listMoneyInOut.filter { it.type == MoneyInOut.MoneyInOutType.IN }.sumOf { it.amount }
        val totalMoneyOut = listMoneyInOut.filter { it.type == MoneyInOut.MoneyInOutType.OUT }.sumOf { it.amount }
        b.tvTotalMoneyInValue.text =
            "${totalMoneyIn.toString().formatNumber(AppData.getMoneyFormat())}  ${AppData.getMoneyUnit()}"
        b.tvTotalMoneyOutValue.text =
            "${totalMoneyOut.toString().formatNumber(AppData.getMoneyFormat())}   ${AppData.getMoneyUnit()}"
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
                        }
                        moneyInOutAdapter.setList(MoneyInOutDB.get.dao().getList())
                        Snackbar.make(b.recyclerView, getString(R.string.deleted),2000)
                            .setAction(R.string.undo) {
                                itemsToDelete.forEach {
                                    it.isDeleted = false
                                    MoneyInOutDB.get.dao().update(it)
                                    moneyInOutAdapter.setList(MoneyInOutDB.get.dao().getList())
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
                listMoneyInOut.add(0, it)
                moneyInOutAdapter.notifyItemChanged(0)
            }
        }
    }

}
