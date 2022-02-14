package promax.dohaumen.financeapp.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogPickItemInListViewBinding
import promax.dohaumen.financeapp.databinding.LayoutAddMoneyIoBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.db.MoneyInOutDB
import promax.dohaumen.financeapp.helper.*
import promax.dohaumen.financeapp.models.*

@SuppressLint("SetTextI18n")
object DialogAddMoneyIO {

    private var bgAlpha = 0.5f

    fun setBgAlpha(alpha: Float): DialogAddMoneyIO {
        this.bgAlpha = alpha
        return this
    }

    fun show(rootLayout: ViewGroup, onSaved: (moneyIO: MoneyInOut) -> Unit = {}) {
        val context = rootLayout.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_add_money_io, rootLayout, false)
        val b = LayoutAddMoneyIoBinding.bind(view)

        b.imgBg.alpha = bgAlpha
        rootLayout.addView(view)

        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_show_dialog)
        val animBg = AnimationUtils.loadAnimation(context, R.anim.anim_show_bg_dialog)
        b.layoutContent.startAnimation(anim)
        b.imgBg.startAnimation(animBg)

        b.imgBg.setOnClickListener {}
        b.layoutContent.setOnClickListener {}
        b.btnCancel.setOnClickListener {
            rootLayout.removeView(view)
        }
        b.tvTimeValue.text = getCurrentTimeStr()

        val moneyTypeAdpater = MoneyTypeAdapter()
        moneyTypeAdpater.modeInDialog = true
        val moneyTypes = mutableListOf<MoneyType>()
        moneyTypeAdpater.setList(moneyTypes)
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        b.recyclerView.adapter = moneyTypeAdpater

        b.radioGrType.setOnCheckedChangeListener { radioGroup, i ->
            if (b.radioTypeMoneyIn.isChecked) {
                b.imgMoneyType.setBackgroundColor(getColor(R.color.green_500))
            } else {
                b.imgMoneyType.setBackgroundColor(getColor(R.color.red_500))
            }
        }
        b.editAmonut.addTextChangedListener {
            b.tvAmonutValue.text = "${it.toString().formatNumber(AppData.getMoneyFormatLiveData().value!!)}  ${AppData.getMoneyUnitLiveData().value}"
        }
        b.tvCurrecyValue.setOnClickListener {
            val b1 = DialogPickItemInListViewBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_pick_item_in_list_view, null)
            )
            val dialog = Dialog(context)
            dialog.setContentView(b1.root)
            b1.tvTitle.text = getStr(R.string.pick_currency)

            val adpater = CurrencyAdapter()
            adpater.hideImgDelete = true
            adpater.setList(CurrencyDB.get.dao().getList())
            adpater.onClickItem = {
                b.tvCurrecyValue.text = it.name
                dialog.cancel()
            }

            b1.recyclerView.layoutManager = LinearLayoutManager(context)
            b1.recyclerView.adapter = adpater

            dialog.show()
        }
        b.btnAddType.setOnClickListener {
            val b1 = DialogPickItemInListViewBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_pick_item_in_list_view, null)
            )
            val dialog = Dialog(context)
            dialog.setContentView(b1.root)
            b1.tvTitle.text = getStr(R.string.pick_type)

            val adapter = MoneyTypeAdapter()
            var list = MoneyTypeDB.get.dao().getList()
            if (b.radioTypeMoneyIn.isChecked) {
                list = list.filter { it.type == MoneyInOut.MoneyInOutType.IN }
            } else {
                list = list.filter { it.type == MoneyInOut.MoneyInOutType.OUT }
            }

            adapter.setList(list.toMutableList())
            adapter.onClickItem = {
                if (!moneyTypes.contains(it)) {
                    moneyTypes.add(it)
                }
                moneyTypeAdpater.notifyDataSetChanged()
                dialog.cancel()
            }
            b1.recyclerView.layoutManager = LinearLayoutManager(context)
            b1.recyclerView.adapter = adapter
            dialog.show()
        }
        b.btnChooseTime.setOnClickListener {
            DialogPickDatetime.show(context) { dateTime: DateTime, s: String ->
                b.tvTimeValue.text = s
            }
        }
        b.btnSave.setOnClickListener {
            val name = b.editName.text.toString()
            val amount = b.editAmonut.text.toString()
            if (name == "" || amount == "") {
                Toast.makeText(context, R.string.please_enter_name_and_amonut, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = if (b.radioTypeMoneyIn.isChecked) {
                MoneyInOut.MoneyInOutType.IN
            } else {
                MoneyInOut.MoneyInOutType.OUT
            }
            val currency = b.tvCurrecyValue.text.toString()
            val desc = b.editDesc.text.toString()
            val datetimeStr = b.tvTimeValue.text.toString()

            val moneyIO = MoneyInOut(
                name = name,
                type = type,
                amount = amount.toLong(),
                currency = currency,
                desc = desc,
                datetime = datetimeStr,
                typeOfSpendingJson = moneyTypeAdpater.getList().toJson()
            )
            MoneyInOutDB.get.dao().insert(moneyIO)
            onSaved(moneyIO)

            if (b.checkboxComputeIntoTheTotalMoney.isChecked) {
                if (type == MoneyInOut.MoneyInOutType.IN) {
                    when (currency) {
                        getStr(R.string.bank) -> {
                            AppData.setTotalMoneyInBanks(AppData.getTotalMoneyInBanks() + moneyIO.amount)
                        }
                        getStr(R.string.cash) -> {
                            AppData.setTotalCash(AppData.getTotalCash() + moneyIO.amount)
                        }
                    }
                } else {
                    when (currency) {
                        getStr(R.string.bank) -> {
                            AppData.setTotalMoneyInBanks(AppData.getTotalMoneyInBanks() - moneyIO.amount)
                        }
                        getStr(R.string.cash) -> {
                            AppData.setTotalCash(AppData.getTotalCash() - moneyIO.amount)
                        }
                    }
                }
            }

            rootLayout.removeView(view)
        }


    }
}
