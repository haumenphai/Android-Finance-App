package promax.dohaumen.financeapp.app_test

import android.util.Log
import org.jetbrains.annotations.TestOnly
import promax.dohaumen.financeapp.models.FilterMoneyIO
import promax.dohaumen.financeapp.models.MoneyInOut

val type = MoneyInOut.MoneyInOutType.IN

class TestFilterMoneyIO {
    companion object {
        // current data = 2022-02-26

        val moneyIOLastYear1 = MoneyInOut("1", type, "1", "bank", datetime = "2021-01-01")
        val moneyIOLastYear2 = MoneyInOut("1", type, "1", "bank", datetime = "2021-05-21 20:21")
        val moneyIOLastYear3 = MoneyInOut("1", type, "1", "bank", datetime = "2021-12-31")

        val moneyIOLastMonth1 = MoneyInOut("1", type, "1", "bank", datetime = "2022-01-01")
        val moneyIOLastMonth2 = MoneyInOut("1", type, "1", "bank", datetime = "2022-01-28 20:21")
        val moneyIOLastMonth3 = MoneyInOut("1", type, "1", "bank", datetime = "2022-01-31")

        val moneyIOLastWeek1 =  MoneyInOut("1", type, "1", "bank", datetime = "2022-02-13")
        val moneyIOLastWeek2 =  MoneyInOut("1", type, "1", "bank", datetime = "2022-02-14 20:00")
        val moneyIOLastWeek3 =  MoneyInOut("1", type, "1", "bank", datetime = "2022-02-19")

        val moneyIOYesterday1 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-25")
        val moneyIOYesterday2 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-25 23:59:59")

        val moneyIOToday1 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-26")
        val moneyIOToday2 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-26 23:59:59")

        val moneyIOThisWeek1 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-20")
        val moneyIOThisWeek2 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-21 20:00")
        val moneyIOThisWeek3 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-26 20:00")
        val moneyIOThisWeek4 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-26")

        val moneyIOThisMonth1 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-01")
        val moneyIOThisMonth2 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-02 20:50:00")
        val moneyIOThisMonth3 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-25 20:50")
        val moneyIOThisMonth4 = MoneyInOut("1", type, "1", "bank", datetime = "2022-02-28")

        val moneyIOThisYear1 = MoneyInOut("1", type, "1", "bank", datetime = "2022-01-01")
        val moneyIOThisYear2 = MoneyInOut("1", type, "1", "bank", datetime = "2022-05-01")
        val moneyIOThisYear3 = MoneyInOut("1", type, "1", "bank", datetime = "2022-05-01 20:20:23")
        val moneyIOThisYear4 = MoneyInOut("1", type, "1", "bank", datetime = "2022-12-31 20:20")
        val moneyIOThisYear5 = MoneyInOut("1", type, "1", "bank", datetime = "2022-12-31")

        val moneyIO2 =  MoneyInOut("1", type, "1", "bank", datetime = "2023-03-20")
        val moneyIO3 =  MoneyInOut("1", type, "1", "bank", datetime = "2020-03-20")
        val moneyIO4 =  MoneyInOut("1", type, "1", "bank", datetime = "")

        val listMoneyIO = listOf(
            moneyIOLastMonth1, moneyIOLastMonth2, moneyIOLastMonth3,
            moneyIOLastYear1, moneyIOLastYear2, moneyIOLastYear3,
            moneyIOLastWeek1, moneyIOLastWeek2, moneyIOLastWeek3,
            moneyIOYesterday1, moneyIOYesterday2,
            moneyIOToday1, moneyIOToday2,
            moneyIOThisWeek1, moneyIOThisWeek2, moneyIOThisWeek3, moneyIOThisWeek4,
            moneyIOThisMonth1, moneyIOThisMonth2, moneyIOThisMonth3, moneyIOThisMonth4,
            moneyIOThisYear1, moneyIOThisYear2, moneyIOThisYear3, moneyIOThisYear4, moneyIOThisYear5,
            moneyIO2, moneyIO3, moneyIO4
        )

        fun testAll() {
            testFilterToday()
            testFilterThisWeek()
            testFilterThisMonth()
            testFilterThisYear()

            testFilterYesterday()
            testFilterLastWeek()
            testFilterLastMonth()
            testFilterLastYear()
        }

        fun testFilterToday() {
            Log.d("APP_TEST", "-------today-----------")
            FilterMoneyIO.getFilterToday().filterMoneyIO(listMoneyIO).forEach {
                Log.d("APP_TEST", it.datetime)
            }
        }

        fun testFilterThisWeek() {
            Log.d("APP_TEST", "-------this week-----------")
            val list = FilterMoneyIO.getFilterThisWeek().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }
        }

        fun testFilterThisMonth() {
            Log.d("APP_TEST", "-------this month-----------")

            val list = FilterMoneyIO.getFilterThisMonth().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }
        }

        fun testFilterThisYear() {
            Log.d("APP_TEST", "-------this year-----------")

            val list = FilterMoneyIO.getFilterThisYear().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }


        }

        fun testFilterYesterday() {
            Log.d("APP_TEST", "-------yesterday-----------")

            val list = FilterMoneyIO.getFilterYesterday().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }
        }

        fun testFilterLastWeek() {
            Log.d("APP_TEST", "-------last week-----------")

            val list = FilterMoneyIO.getFilterLastWeek().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }
        }

        fun testFilterLastMonth() {
            Log.d("APP_TEST", "-------last month-----------")

            val list = FilterMoneyIO.getFilterLastMonth().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }

        }

        fun testFilterLastYear() {
            Log.d("APP_TEST", "-------last year-----------")

            val list = FilterMoneyIO.getFilterLastYear().filterMoneyIO(listMoneyIO)
            FilterMoneyIO.getItemSortByDatetime().sortMoneyIO(list).forEach {
                Log.d("APP_TEST", it.datetime)
            }
        }
    }
}