package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import promax.dohaumen.financeapp.MyApp.Companion.context
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogReportDetailBinding
import promax.dohaumen.financeapp.databinding.DialogReportMoneyIoBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.helper.getIndex
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.helper.setTextBold
import promax.dohaumen.financeapp.helper.toYMD
import promax.dohaumen.financeapp.models.*

class DialogReportMoneyIO(context: Context) {
    val dialog = Dialog(context)
    val b = DialogReportMoneyIoBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_report_money_io, null))
    private val adpater = ReportMoneyIOAdapter()
    private var list = ReportMoneyIO.getListReport()
    private var listMoneyIO = listOf<MoneyInOut>()
    private val dialogReportDetail = DialogReportMoneyIODetail(context)

    private var onPreProcessShowDetail: () -> Unit = {}
    private var onProcessShowDetailComplete: () -> Unit = {}

    fun setOnPreProcessShowDetail(callback: () -> Unit): DialogReportMoneyIO {
        this.onPreProcessShowDetail = callback
        return this
    }
    fun setOnProcessShowDetailComplete(callback: () -> Unit): DialogReportMoneyIO {
        this.onProcessShowDetailComplete = callback
        return this
    }


    private var job: Job? = null
    fun setListMoneyIO(list: List<MoneyInOut>, onComplete:() -> Unit): DialogReportMoneyIO {
        this.listMoneyIO = list
        job?.cancel()
        job = GlobalScope.launch {
            processMoneyIO()
            withContext(Dispatchers.Main) {
                adpater.setList(this@DialogReportMoneyIO.list)
                adpater.onClickButtonDetail = {
                    dialogReportDetail.setTitle(it.name)
                    dialogReportDetail.listMoneyIO = it.listMoneyIOFiltered

                    onPreProcessShowDetail()
                    dialogReportDetail.show() {
                        onProcessShowDetailComplete()
                    }
                }
                onComplete()
            }
        }
        return this
    }

    fun cancelJob() {
        job?.cancel()
        dialogReportDetail.cancelJob()
    }


    private fun processMoneyIO() {
        list.forEach { reportMoneyIO ->
            var listMoneyIOFiltered = listOf<MoneyInOut>()

            when (reportMoneyIO.name) {
                ReportMoneyIO.REPORT_TODAY -> listMoneyIOFiltered = FilterMoneyIO.getFilterToday().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_THIS_WEEK -> listMoneyIOFiltered = FilterMoneyIO.getFilterThisWeek().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_THIS_MONTH -> listMoneyIOFiltered = FilterMoneyIO.getFilterThisMonth().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_THIS_YEAR -> listMoneyIOFiltered = FilterMoneyIO.getFilterThisYear().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_YESTERDAY -> listMoneyIOFiltered = FilterMoneyIO.getFilterYesterday().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_LAST_WEEK -> listMoneyIOFiltered = FilterMoneyIO.getFilterLastWeek().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_LAST_MONTH -> listMoneyIOFiltered = FilterMoneyIO.getFilterLastMonth().filterMoneyIO(listMoneyIO)
                ReportMoneyIO.REPORT_LAST_YEAR -> listMoneyIOFiltered = FilterMoneyIO.getFilterLastYear().filterMoneyIO(listMoneyIO)
            }
            if (reportMoneyIO.isEndList) {
                listMoneyIOFiltered = listMoneyIO
                // compute name
                val listSortedByDatetime =
                    FilterMoneyIO.getItemSortByDatetime()
                        .sortMoneyIO(listMoneyIO)
                var reportName = getStr(R.string.all)
                if (listSortedByDatetime.isNotEmpty()) {
                    reportName = "${getStr(R.string.all)} (${listSortedByDatetime[0].datetime.toYMD()} - " +
                                  "${listSortedByDatetime[listSortedByDatetime.size - 1].datetime.toYMD()})"
                }
                reportMoneyIO.name = reportName
            }

            reportMoneyIO.apply {
                this.listMoneyIOFiltered = listMoneyIOFiltered
                this.totalMoneyIn = listMoneyIOFiltered.sumMoneyIn()
                this.totalMoneyOut = listMoneyIOFiltered.sumMoneyOut()
            }
        }
    }

    init {
        dialog.setContentView(b.root)
        b.recyclerView.layoutManager = LinearLayoutManager(context)
        b.recyclerView.adapter = adpater
    }

    fun show(): DialogReportMoneyIO {
        dialog.show()
        return this
    }

}

class DialogReportMoneyIODetail(context: Context) {
    val dialog = Dialog(context)
    val b = DialogReportDetailBinding.bind(
        LayoutInflater.from(context).inflate(
            R.layout.dialog_report_detail,
            null
        )
    )
    var listMoneyIO: List<MoneyInOut> = listOf()

    init {
        dialog.setContentView(b.root)
    }

    fun setTitle(title: String) {
        b.tvTitle.text = title
    }

    var job: Job? = null
    fun show(onComplete: () -> Unit) {
        job?.cancel()
        job = GlobalScope.launch {
            val totalMoneyIn = "+${AppData.formatMoneyWithAppConfig(listMoneyIO.sumMoneyIn())}"
            val totalMoneyOut = "-${AppData.formatMoneyWithAppConfig(listMoneyIO.sumMoneyOut())}"

            val totalCashIn = "+${AppData.formatMoneyWithAppConfig(listMoneyIO.sumCashIn())}"
            val totalCashOut = "-${AppData.formatMoneyWithAppConfig(listMoneyIO.sumCashOut())}"
            val cashInCount = listMoneyIO.cashInCount()
            val cashOutCount = listMoneyIO.cashOutCount()

            val totalMoneyBankIn = "+${AppData.formatMoneyWithAppConfig(listMoneyIO.sumMoneyBankIn())}"
            val totalMoneyBankOut = "-${AppData.formatMoneyWithAppConfig(listMoneyIO.sumMoneyBankOut())}"
            val bankInCount = listMoneyIO.bankInCount()
            val bankOutCount = listMoneyIO.bankOutCount()

            var content =
                "${getStr(R.string.total_money_in)} $totalMoneyIn\n" +
                        "${getStr(R.string.total_money_out)} $totalMoneyOut\n" +
                        "----------------------------------------------\n" +
                        "${getStr(R.string.cash1)}\n" +
                        "$totalCashIn   ($cashInCount)\n" +
                        "$totalCashOut   ($cashOutCount)\n" +
                        "----------------------------------------------\n" +
                        "${getStr(R.string.money_in_bank1)}\n" +
                        "$totalMoneyBankIn   ($bankInCount)\n" +
                        "$totalMoneyBankOut   ($bankOutCount)\n" +
                        "----------------------------------------------\n"
            val listTypeStr = mutableListOf<String>()
            listMoneyIO.getMoneyByType().forEach {
                listTypeStr.add(it["name"]!!)
                content += "${it["name"]}: ${it["amount"]}   (${it["count"]})\n"
            }

            withContext(Dispatchers.Main) {
                b.tvContent.text = content
                b.tvContent.setTextBold(
                    getStr(R.string.total_money_in),
                    getStr(R.string.total_money_out),
                    getStr(R.string.cash1),
                    getStr(R.string.money_in_bank1),
                )
                listTypeStr.forEach {
                    b.tvContent.setTextBold(it)
                }
                onComplete()
                dialog.show()
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}
