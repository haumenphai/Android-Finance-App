package promax.dohaumen.financeapp.datas

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.annotations.NotNull
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.helper.formatNumber
import java.math.BigDecimal

object AppData {
    private val sharefs: SharedPreferences =
        MyApp.context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    private val totalMoneyInBanksLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("total_money_in_banks1", "0"))
    private val totalCashLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("total_cash1", "0"))
    private val totalMoneyLiveData: MutableLiveData<String> = MutableLiveData()
    private val moneyUnitLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("money_unit1", "đ"))
    private val moneyFormatLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("money_format1", "."))

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

    fun getMoneyFormatLiveData(): LiveData<String> = moneyFormatLiveData
    fun getMoneyFormat(): Char = moneyFormatLiveData.value!![0]

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
                "${getTotalMoneyInBanks().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }
        totalCashLiveData.observeForever {
            totalMoneyLiveData.value = (BigDecimal(getTotalCash()) + BigDecimal(getTotalMoneyInBanks())).toPlainString()
            totalCashFormatedLiveData.value =
                "${getTotalCash().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }
        totalMoneyLiveData.observeForever {
            totalMoneyFormatedLiveData.value =
                "${getTotalMoney().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }

        fun reLoadMoneyFormat() {
            totalMoneyInBanksFormatedLiveData.value =
                "${getTotalMoneyInBanks().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
            totalCashFormatedLiveData.value =
                "${getTotalCash().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
            totalMoneyFormatedLiveData.value =
                "${getTotalMoney().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }

        moneyUnitLiveData.observeForever {
           reLoadMoneyFormat()
        }
        moneyFormatLiveData.observeForever {
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
        moneyFormatLiveData.value = moneyFormat.toString()
    }

    fun formatMoneyWithAppConfig(money: String) = "${money.formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"

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

}
