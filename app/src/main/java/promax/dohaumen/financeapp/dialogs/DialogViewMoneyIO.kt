package promax.dohaumen.financeapp.dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogViewMoneyIoBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.helper.formatNumber
import promax.dohaumen.financeapp.models.MoneyInOut
import promax.dohaumen.financeapp.models.MoneyTypeAdapter

object DialogViewMoneyIO {

    @SuppressLint("SetTextI18n")
    fun show(rootLayout: ViewGroup, moneyIO: MoneyInOut, onEditComplete: (moneyIO: MoneyInOut) -> Unit = {}) {
        val view = LayoutInflater.from(rootLayout.context)
            .inflate(R.layout.dialog_view_money_io, rootLayout, false)
        val b = DialogViewMoneyIoBinding.bind(view)

        rootLayout.addView(view)

        val anim = AnimationUtils.loadAnimation(view.context, R.anim.anim_show_dialog)
        val animBg = AnimationUtils.loadAnimation(view.context, R.anim.anim_show_bg_dialog)
        b.layoutContent.startAnimation(anim)
        b.imgBg.startAnimation(animBg)

        b.tvName.text = moneyIO.name
        if (moneyIO.type == MoneyInOut.MoneyInOutType.IN) {
            b.tvAmonut.text = "+${AppData.formatMoneyWithAppConfig(moneyIO.amount)}"
        } else {
            b.tvAmonut.text = "-${AppData.formatMoneyWithAppConfig(moneyIO.amount)}"
        }
        b.tvCurrecyValue.text = moneyIO.currency
        b.tvDatetimeValue.text = moneyIO.datetime
        b.tvDescValue.text = moneyIO.desc

        val moneyTypeAdapter = MoneyTypeAdapter()
        moneyTypeAdapter.modeInDialog = true
        moneyTypeAdapter.hideIconDelete = true
        moneyTypeAdapter.setList(moneyIO.getListMoneyType().toMutableList())

        b.recyclerView.layoutManager = LinearLayoutManager(rootLayout.context)
        b.recyclerView.adapter = moneyTypeAdapter


        b.imgBg.setOnClickListener {
            rootLayout.removeView(b.root)
        }
        b.imgEdit.setOnClickListener {
            DialogAddMoneyIO.showDialogEdit(moneyIO, rootLayout) {
                rootLayout.removeView(b.root)
                onEditComplete(it)
                show(rootLayout, it, onEditComplete)
            }
        }
    }
}
