package promax.dohaumen.financeapp.datas

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.helper.formatNumber
import promax.dohaumen.financeapp.models.Currency
import promax.dohaumen.financeapp.models.MoneyInOut
import java.math.BigDecimal

object AppData {
    private val sharefs: SharedPreferences =
        MyApp.context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    private val totalMoneyInBanksLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("total_money_in_banks1", "0"))
    private val totalCashLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("total_cash1", "0"))
    private val totalMoneyLiveData: MutableLiveData<String> = MutableLiveData()
    private val moneyUnitLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("money_unit1", "đ"))

    private val totalMoneyFormatedLiveData: MutableLiveData<String> = MutableLiveData()
    private val totalMoneyInBanksFormatedLiveData: MutableLiveData<String> = MutableLiveData()
    private val totalCashFormatedLiveData: MutableLiveData<String> = MutableLiveData()

    fun getTotalMoneyInBanksLiveData(): LiveData<String> = totalMoneyInBanksLiveData
    fun getTotalMoneyInBanks(): String = totalMoneyInBanksLiveData.value!!

    fun getTotalCashLiveData(): LiveData<String> = totalCashLiveData
    fun getTotalCash(): String = totalCashLiveData.value!!

    fun getTotalMoneyLiveData(): LiveData<String> = totalMoneyLiveData
    fun getTotalMoney(): String = totalMoneyLiveData.value!!

    fun getMoneyUnitLiveData(): LiveData<String> = moneyUnitLiveData
    fun getMoneyUnit(): String = moneyUnitLiveData.value!!

    fun getTotalMoneyFormatedLiveData(): LiveData<String> = totalMoneyFormatedLiveData
    fun getTotalMoneyFormated(): String = totalMoneyFormatedLiveData.value!!

    fun getTotalMoneyInBanksFormatedLiveData(): LiveData<String> = totalMoneyInBanksFormatedLiveData
    fun getTotalMoneyInBanksFormated(): String = totalMoneyInBanksFormatedLiveData.value!!

    fun getTotalCashFormatedLiveData(): LiveData<String> = totalCashFormatedLiveData
    fun getTotalCashFormated(): String = totalCashFormatedLiveData.value!!


    init {
        totalMoneyInBanksLiveData.observeForever {
            totalMoneyLiveData.value = (BigDecimal(getTotalCash()) + BigDecimal(getTotalMoneyInBanks())).toPlainString()
            totalMoneyInBanksFormatedLiveData.value =
                "${getTotalMoneyInBanks().formatNumber()}  ${getMoneyUnit()}"
        }
        totalCashLiveData.observeForever {
            totalMoneyLiveData.value = (BigDecimal(getTotalCash()) + BigDecimal(getTotalMoneyInBanks())).toPlainString()
            totalCashFormatedLiveData.value =
                "${getTotalCash().formatNumber()}  ${getMoneyUnit()}"
        }
        totalMoneyLiveData.observeForever {
            totalMoneyFormatedLiveData.value =
                "${getTotalMoney().formatNumber()}  ${getMoneyUnit()}"
        }

        fun reLoadMoneyFormat() {
            totalMoneyInBanksFormatedLiveData.value =
                "${getTotalMoneyInBanks().formatNumber()}  ${getMoneyUnit()}"
            totalCashFormatedLiveData.value =
                "${getTotalCash().formatNumber()}  ${getMoneyUnit()}"
            totalMoneyFormatedLiveData.value =
                "${getTotalMoney().formatNumber()}  ${getMoneyUnit()}"
        }

        moneyUnitLiveData.observeForever {
           reLoadMoneyFormat()
        }
    }

    fun setTotalMoneyInBanks(money: String) {
        sharefs.edit().putString("total_money_in_banks1", money).apply()
        totalMoneyInBanksLiveData.value = money
    }
    fun setTotalCash(money: String) {
        sharefs.edit().putString("total_cash1", money).apply()
        totalCashLiveData.value = money
    }

    fun setMoneyUnit(moneyUnit: String) {
        sharefs.edit().putString("money_unit1", moneyUnit).apply()
        moneyUnitLiveData.value = moneyUnit
    }

    fun setMoneyFormat(moneyFormat: Char) {
        sharefs.edit().putString("money_format1", moneyFormat.toString()).apply()
    }

    fun formatMoneyWithAppConfig(money: String) = "${money.formatNumber()}  ${getMoneyUnit()}"

    fun increaseMoneyBank(money: String) {
        setTotalMoneyInBanks(
            (BigDecimal(getTotalMoneyInBanks()) + BigDecimal(money)).toPlainString()
        )
    }

    fun minusMoneyBank(money: String) {
        setTotalMoneyInBanks((BigDecimal(getTotalMoneyInBanks()) - BigDecimal(money)).toPlainString())
    }

    fun increaseTotalCash(money: String) {
        setTotalCash((BigDecimal(getTotalCash()) + BigDecimal(money)).toPlainString())
    }

    fun minusTotalCash(money: String) {
        setTotalCash((BigDecimal(getTotalCash()) - BigDecimal(money)).toPlainString())
    }


    fun refundTheAmount(moneyInOut: MoneyInOut) {
        if (!moneyInOut.computeIntoTheTotalMoney) return
        if (moneyInOut.type == MoneyInOut.MoneyInOutType.IN) {
            if (moneyInOut.currency == Currency.BANK) {
                minusMoneyBank(moneyInOut.amount)
            } else if (moneyInOut.currency == Currency.CASH) {
                minusTotalCash(moneyInOut.amount)
            }
        } else {
            if (moneyInOut.currency == Currency.BANK) {
                increaseMoneyBank(moneyInOut.amount)
            } else if (moneyInOut.currency == Currency.CASH) {
                increaseTotalCash(moneyInOut.amount)
            }
        }
    }

    fun calculateIntoTheAmount(moneyInOut: MoneyInOut) {
        if (!moneyInOut.computeIntoTheTotalMoney) return
        if (moneyInOut.type == MoneyInOut.MoneyInOutType.IN) {
            if (moneyInOut.currency == Currency.BANK) {
                increaseMoneyBank(moneyInOut.amount)
            } else if (moneyInOut.currency == Currency.CASH) {
                increaseTotalCash(moneyInOut.amount)
            }
        } else {
            if (moneyInOut.currency == Currency.BANK) {
                minusMoneyBank(moneyInOut.amount)
            } else if (moneyInOut.currency == Currency.CASH) {
                minusTotalCash(moneyInOut.amount)
            }
        }
    }
}
