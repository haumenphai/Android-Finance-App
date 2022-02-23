package promax.dohaumen.financeapp.models

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.ItemReportMoneyIoBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.helper.getStr

class ReportMoneyIO {
    var name = ""
    var totalMoneyIn = ""
    var totalMoneyOut = ""

    var showMoreDetail = false
    var isEndList = false
    var cashIn = ""
    var cashOut = ""
    var moneyBankIn = ""
    var moneyBankOut = ""
    var moneyInCount = 0
    var moneyOutCount = 0

    constructor()
    constructor(name: String, isEndList: Boolean = false) {
        this.name = name
        this.isEndList = isEndList
    }

    companion object {
        val REPORT_TODAY = getStr(R.string.today)
        val REPORT_THIS_WEEK = getStr(R.string.this_week)
        val REPORT_THIS_MONTH = getStr(R.string.this_month)
        val REPORT_THIS_YEAR = getStr(R.string.this_year)

        val REPORT_YESTERDAY = getStr(R.string.yesterday)
        val REPORT_LAST_WEEK = getStr(R.string.last_week)
        val REPORT_LAST_MONTH = getStr(R.string.last_month)
        val REPORT_LAST_YEAR = getStr(R.string.last_year)
        val REPORT_ALL = getStr(R.string.all)

        fun getListReport() = listOf(
            ReportMoneyIO(REPORT_TODAY),
            ReportMoneyIO(REPORT_THIS_WEEK),
            ReportMoneyIO(REPORT_THIS_MONTH),
            ReportMoneyIO(REPORT_THIS_YEAR),
            ReportMoneyIO(REPORT_YESTERDAY),
            ReportMoneyIO(REPORT_LAST_WEEK),
            ReportMoneyIO(REPORT_LAST_MONTH),
            ReportMoneyIO(REPORT_LAST_YEAR),
            ReportMoneyIO(REPORT_ALL, isEndList = true),
        )
    }
}

class ReportMoneyIOAdapter: RecyclerView.Adapter<ReportMoneyIOAdapter.ReportMoneyIOHolder>() {
    private var list = listOf<ReportMoneyIO>()

    fun setList(list: List<ReportMoneyIO>) {
        this.list = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportMoneyIOHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report_money_io, parent, false)
        return ReportMoneyIOHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReportMoneyIOHolder, position: Int) {
        val reportMoneyIO = list[position]
        val b = holder.b

        b.tvName.text = reportMoneyIO.name
        b.tvTotalMoneyInValue.text = "+${AppData.formatMoneyWithAppConfig(reportMoneyIO.totalMoneyIn)}"
        b.tvTotalMoneyOutValue.text = "-${AppData.formatMoneyWithAppConfig(reportMoneyIO.totalMoneyOut)}"

        if (!reportMoneyIO.showMoreDetail) {
            b.layoutMoreDetail.visibility = View.GONE
        } else {
            b.layoutMoreDetail.visibility = View.VISIBLE

            b.tvCashInTotalValue.text = "+${AppData.formatMoneyWithAppConfig(reportMoneyIO.cashIn)}"
            b.tvCashOutTotalValue.text = "-${AppData.formatMoneyWithAppConfig(reportMoneyIO.cashOut)}"
            b.tvMoneyInBankTotalValueIn.text = "+${AppData.formatMoneyWithAppConfig(reportMoneyIO.moneyBankIn)}"
            b.tvMoneyInBankTotalValueOut.text ="-${AppData.formatMoneyWithAppConfig(reportMoneyIO.moneyBankOut)}"
            b.tvMoneyInTimesCountValue.text = reportMoneyIO.moneyInCount.toString()
            b.tvMoneyOutTimesCountValue.text = reportMoneyIO.moneyOutCount.toString()
        }
    }

    override fun getItemCount(): Int = list.size

    class ReportMoneyIOHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ItemReportMoneyIoBinding.bind(itemView)
    }
}
