package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogPickItemInListViewBinding
import promax.dohaumen.financeapp.databinding.LayoutAddMoneyIoBinding
import promax.dohaumen.financeapp.helper.getColor
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.models.*

object DialogAddMoneyIO {

    private var bgAlpha = 0.5f

    fun setBgAlpha(alpha: Float): DialogAddMoneyIO {
        this.bgAlpha = alpha
        return this
    }

    fun show(rootLayout: ViewGroup) {
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
            adapter.hideIconDelete = true
            adapter.setList(MoneyTypeDB.get.dao().getList().toMutableList())
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
    }
}
