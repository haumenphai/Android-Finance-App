package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowInsets.Side.all
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogReportMoneyIoBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.helper.toYMD
import promax.dohaumen.financeapp.models.*
import java.util.logging.Filter

class DialogReportMoneyIO(context: Context) {
    val dialog = Dialog(context)
    val b = DialogReportMoneyIoBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_report_money_io, null))
    private val adpater = ReportMoneyIOAdapter()
    private var list = ReportMoneyIO.getListReport()
    private var listMoneyIO = listOf<MoneyInOut>()

    fun setListMoneyIO(list: List<MoneyInOut>): DialogReportMoneyIO {
        this.listMoneyIO = list
        processMoneyIO()
        adpater.setList(this.list)
        return this
    }

    private fun processMoneyIO() {
        list.forEach { reportMoneyIO ->
            when (reportMoneyIO.name) {
                ReportMoneyIO.REPORT_TODAY -> {
                    val moneyIOToday = FilterMoneyIO.getFilterToday().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOToday.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOToday.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_THIS_WEEK -> {
                    val moneyIOThisWeek = FilterMoneyIO.getFilterThisWeek().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOThisWeek.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOThisWeek.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_THIS_MONTH -> {
                    val moneyIOThisMonth = FilterMoneyIO.getFilterThisMonth().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOThisMonth.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOThisMonth.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_THIS_YEAR -> {
                    val moneyIOThisYear = FilterMoneyIO.getFilterThisYear().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOThisYear.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOThisYear.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_YESTERDAY -> {
                    val moneyIOYesterday = FilterMoneyIO.getFilterYesterday().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOYesterday.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOYesterday.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_LAST_WEEK -> {
                    val moneyIOLastWeek = FilterMoneyIO.getFilterLastWeek().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOLastWeek.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOLastWeek.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_LAST_MONTH -> {
                    val moneyIOLastMonth = FilterMoneyIO.getFilterLastMonth().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOLastMonth.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOLastMonth.sumMoneyOut()
                }
                ReportMoneyIO.REPORT_LAST_YEAR -> {
                    val moneyIOLastYear = FilterMoneyIO.getFilterLastYear().filterMoneyIO(listMoneyIO)
                    reportMoneyIO.totalMoneyIn = moneyIOLastYear.sumMoneyIn()
                    reportMoneyIO.totalMoneyOut = moneyIOLastYear.sumMoneyOut()
                }
            }
            if (reportMoneyIO.isEndList) {
                val listSortedByDatetime =
                    FilterMoneyIO.getItemSortByDatetime()
                        .sortMoneyIO(listMoneyIO).filter { it.datetime != "" }
                var reportName = getStr(R.string.all)
                if (listSortedByDatetime.isNotEmpty()) {
                    reportName = "${getStr(R.string.all)} (${listSortedByDatetime[0].datetime.toYMD()} - " +
                                  "${listSortedByDatetime[listSortedByDatetime.size - 1].datetime.toYMD()})"
                }
                reportMoneyIO.apply {
                    this.showMoreDetail = true
                    this.name = reportName
                    this.totalMoneyIn = listMoneyIO.sumMoneyIn()
                    this.totalMoneyOut = listMoneyIO.sumMoneyOut()
                    this.cashIn = listMoneyIO.sumCashIn()
                    this.cashOut = listMoneyIO.sumCashOut()
                    this.moneyBankIn = listMoneyIO.sumMoneyBankIn()
                    this.moneyBankOut = listMoneyIO.sumMoneyBankOut()
                    this.moneyInCount = listMoneyIO.moneyInCount()
                    this.moneyOutCount = listMoneyIO.moneyOutCount()
                }

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
