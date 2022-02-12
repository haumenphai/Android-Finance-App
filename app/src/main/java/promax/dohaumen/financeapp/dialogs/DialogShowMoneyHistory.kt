package promax.dohaumen.financeapp.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogShowMoneyHistoryBinding
import promax.dohaumen.financeapp.models.*

class DialogShowMoneyHistory {
    fun show(rootLayout: ViewGroup) {
        val context = rootLayout.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_show_money_history, rootLayout, false)
        val b = DialogShowMoneyHistoryBinding.bind(view)
        rootLayout.addView(view)

        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_show_dialog)
        val animBg = AnimationUtils.loadAnimation(context, R.anim.anim_show_bg_dialog)
        b.layoutContent.startAnimation(anim)
        b.imgBg.startAnimation(animBg)
        b.imgBg.setOnClickListener { rootLayout.removeView(view) }

        val adapter1 = TotalMoneyInBanksHistoryAdpater()
        adapter1.setList(TotalMoneyInBanksHistoryDB.get.dao().getList())
        b.recyclerViewTotalMoneyInBanksHistory.layoutManager = LinearLayoutManager(context)
        b.recyclerViewTotalMoneyInBanksHistory.adapter = adapter1

        val adapter2 = TotalCashHistoryAdapter()
        adapter2.setList(TotalCashHistoryDB.get.dao().getList())
        b.recyclerViewTotalCashHistory.layoutManager = LinearLayoutManager(context)
        b.recyclerViewTotalCashHistory.adapter = adapter2
    }
}
