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

    var isEndList = false
    var listMoneyIOFiltered = listOf<MoneyInOut>()

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

    var onClickButtonDetail: (report: ReportMoneyIO) -> Unit = {}

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
    }

    override fun getItemCount(): Int = list.size

    inner class ReportMoneyIOHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ItemReportMoneyIoBinding.bind(itemView)
        init {
            b.btnDetail.setOnClickListener {
                onClickButtonDetail(list[layoutPosition])
            }
        }
    }
}
