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

        val moneyTypeAdapter = MoneyTypeAdapter()
        moneyTypeAdapter.modeInDialog = true
        val moneyTypes = mutableListOf<MoneyType>()
        moneyTypeAdapter.setList(moneyTypes)
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        b.recyclerView.adapter = moneyTypeAdapter

        b.radioGrType.setOnCheckedChangeListener { radioGroup, i ->
            if (b.radioTypeMoneyIn.isChecked) {
                b.imgMoneyType.setBackgroundColor(getColor(R.color.green_500))
            } else {
                b.imgMoneyType.setBackgroundColor(getColor(R.color.red_500))
            }
        }
        b.editAmonut.addTextChangedListener {
            b.tvAmonutValue.text = AppData.formatMoneyWithAppConfig(it.toString())
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
                moneyTypeAdapter.notifyDataSetChanged()
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
            if (!amount.isNumeric()) {
                Toast.makeText(context, R.string.please_enter_a_number, Toast.LENGTH_SHORT).show()
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
                amount = amount,
                currency = currency,
                desc = desc,
                datetime = datetimeStr,
                typeOfSpendingJson = moneyTypeAdapter.getList().toJson(),
                computeIntoTheTotalMoney = b.checkboxComputeIntoTheTotalMoney.isChecked
            )
            MoneyInOutDB.get.dao().insert(moneyIO)
            onSaved(moneyIO)

            if (b.checkboxComputeIntoTheTotalMoney.isChecked) {
                if (type == MoneyInOut.MoneyInOutType.IN) {
                    when (currency) {
                        getStr(R.string.bank) -> {
                            AppData.increaseMoneyBank(moneyIO.amount)
                        }
                        getStr(R.string.cash) -> {
                            AppData.increaseTotalCash(moneyIO.amount)
                        }
                    }
                } else {
                    when (currency) {
                        getStr(R.string.bank) -> {
                            AppData.minusMoneyBank(moneyIO.amount)
                        }
                        getStr(R.string.cash) -> {
                            AppData.minusTotalCash(moneyIO.amount)
                        }
                    }
                }
            }

            rootLayout.removeView(view)
        }


    }

    fun showDialogEdit(moneyIO: MoneyInOut, rootLayout: ViewGroup, onSaved: (moneyIO: MoneyInOut) -> Unit = {}) {
        val oldMoneyIO = moneyIO.copy()

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

        b.apply {
            this.editName.setText(moneyIO.name)
            this.editAmonut.setText(moneyIO.amount)
            if (moneyIO.type == MoneyInOut.MoneyInOutType.IN) {
                radioTypeMoneyIn.isChecked = true
            } else {
                radioTypeMoneyOut.isChecked = true
            }
            this.tvCurrecyValue.text = moneyIO.currency
            this.tvTimeValue.text = moneyIO.datetime
            this.editDesc.setText(moneyIO.desc)
            this.checkboxComputeIntoTheTotalMoney.isChecked = moneyIO.computeIntoTheTotalMoney
        }

        val moneyTypeAdapter = MoneyTypeAdapter()
        moneyTypeAdapter.modeInDialog = true
        val moneyTypes = moneyIO.getListMoneyType().toMutableList()
        moneyTypeAdapter.setList(moneyTypes)
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        b.recyclerView.adapter = moneyTypeAdapter

        b.radioGrType.setOnCheckedChangeListener { radioGroup, i ->
            if (b.radioTypeMoneyIn.isChecked) {
                b.imgMoneyType.setBackgroundColor(getColor(R.color.green_500))
            } else {
                b.imgMoneyType.setBackgroundColor(getColor(R.color.red_500))
            }
        }
        b.editAmonut.addTextChangedListener {
            b.tvAmonutValue.text = AppData.formatMoneyWithAppConfig(it.toString())
        }
        b.tvCurrecyValue.setOnClickListener {
            val b1 = DialogPickItemInListViewBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_pick_item_in_list_view, null)
            )
            val dialog = Dialog(context)
            dialog.setContentView(b1.root)
            b1.tvTitle.text = getStr(R.string.pick_currency)

            val adapter = CurrencyAdapter()
            adapter.hideImgDelete = true
            adapter.setList(CurrencyDB.get.dao().getList())
            adapter.onClickItem = {
                b.tvCurrecyValue.text = it.name
                dialog.cancel()
            }

            b1.recyclerView.layoutManager = LinearLayoutManager(context)
            b1.recyclerView.adapter = adapter

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
                moneyTypeAdapter.notifyDataSetChanged()
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
            if (!amount.isNumeric()) {
                Toast.makeText(context, R.string.please_enter_a_number, Toast.LENGTH_SHORT).show()
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

            moneyIO.apply {
                this.name = name
                this.type = type
                this.amount = amount
                this.currency = currency
                this.desc = desc
                this.datetime = datetimeStr
                this.typeOfSpendingJson = moneyTypeAdapter.getList().toJson()
                this.computeIntoTheTotalMoney = b.checkboxComputeIntoTheTotalMoney.isChecked
            }
            MoneyInOutDB.get.dao().update(moneyIO)
            onSaved(moneyIO)


            if (moneyIO.computeIntoTheTotalMoney) {

                // refund the calculation to total money
                if (oldMoneyIO.computeIntoTheTotalMoney) {
                    if (oldMoneyIO.type == MoneyInOut.MoneyInOutType.IN) {
                        when (oldMoneyIO.currency) {
                            getStr(R.string.bank) -> {
                                AppData.minusMoneyBank(oldMoneyIO.amount)
                            }
                            getStr(R.string.cash) -> {
                                AppData.minusTotalCash(oldMoneyIO.amount)
                            }
                        }
                    } else {
                        when (oldMoneyIO.currency) {
                            getStr(R.string.bank) -> {
                                AppData.increaseMoneyBank(oldMoneyIO.amount)
                            }
                            getStr(R.string.cash) -> {
                                AppData.increaseTotalCash(oldMoneyIO.amount)
                            }
                        }
                    }
                }

                // new calculation to total money
                if (type == MoneyInOut.MoneyInOutType.IN) {
                    when (moneyIO.currency) {
                        getStr(R.string.bank) -> {
                            AppData.increaseMoneyBank(moneyIO.amount)
                        }
                        getStr(R.string.cash) -> {
                            AppData.increaseTotalCash(moneyIO.amount)
                        }
                    }
                } else {
                    when (moneyIO.currency) {
                        getStr(R.string.bank) -> {
                            AppData.minusMoneyBank(moneyIO.amount)
                        }
                        getStr(R.string.cash) -> {
                            AppData.minusTotalCash(moneyIO.amount)
                        }
                    }
                }
            }

            rootLayout.removeView(view)
        }


    }
}
