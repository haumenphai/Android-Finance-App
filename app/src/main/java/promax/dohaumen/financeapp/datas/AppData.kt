package promax.dohaumen.financeapp.datas

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.annotations.NotNull
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.helper.formatNumber

object AppData {
    private val sharefs: SharedPreferences =
        MyApp.context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    private val totalMoneyInBanksLiveData: MutableLiveData<Long> = MutableLiveData(sharefs.getLong("total_money_in_banks", 0L))
    private val totalCashLiveData: MutableLiveData<Long> = MutableLiveData(sharefs.getLong("total_cash", 0L))
    private val totalMoneyLiveData: MutableLiveData<Long> = MutableLiveData()
    private val moneyUnitLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("money_unit", "đ"))
    private val moneyFormatLiveData: MutableLiveData<String> = MutableLiveData(sharefs.getString("money_format", "."))

    private val totalMoneyFormatedLiveData: MutableLiveData<String> = MutableLiveData()
    private val totalMoneyInBanksFormatedLiveData: MutableLiveData<String> = MutableLiveData()
    private val totalCashFormatedLiveData: MutableLiveData<String> = MutableLiveData()

    fun getTotalMoneyInBanksLiveData(): LiveData<Long> = totalMoneyInBanksLiveData
    fun getTotalMoneyInBanks(): Long = totalMoneyInBanksLiveData.value!!

    fun getTotalCashLiveData(): LiveData<Long> = totalCashLiveData
    fun getTotalCash(): Long = totalCashLiveData.value!!

    fun getTotalMoneyLiveData(): LiveData<Long> = totalMoneyLiveData
    fun getTotalMoney(): Long = totalMoneyLiveData.value!!

    fun getMoneyUnitLiveData(): LiveData<String> = moneyUnitLiveData
    fun getMoneyUnit(): String = moneyUnitLiveData.value!!

    fun getMoneyFormatLiveData(): LiveData<String> = moneyFormatLiveData
    fun getMoneyFormat(): String = moneyFormatLiveData.value!!

    fun getTotalMoneyFormatedLiveData(): LiveData<String> = totalMoneyFormatedLiveData
    fun getTotalMoneyFormated(): String = totalMoneyFormatedLiveData.value!!

    fun getTotalMoneyInBanksFormatedLiveData(): LiveData<String> = totalMoneyInBanksFormatedLiveData
    fun getTotalMoneyInBanksFormated(): String = totalMoneyInBanksFormatedLiveData.value!!

    fun getTotalCashFormatedLiveData(): LiveData<String> = totalCashFormatedLiveData
    fun getTotalCashFormated(): String = totalCashFormatedLiveData.value!!


    init {
        totalMoneyInBanksLiveData.observeForever {
            totalMoneyLiveData.value = getTotalCash() + getTotalMoneyInBanks()
            totalMoneyInBanksFormatedLiveData.value =
                "${getTotalMoneyInBanks().toString().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }
        totalCashLiveData.observeForever {
            totalMoneyLiveData.value = getTotalCash() + getTotalMoneyInBanks()
            totalCashFormatedLiveData.value =
                "${getTotalCash().toString().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }
        totalMoneyLiveData.observeForever {
            totalMoneyFormatedLiveData.value =
                "${getTotalMoney().toString().formatNumber(getMoneyFormat())}  ${getMoneyUnit()}"
        }
    }

    fun setTotalMoneyInBanks(money: Long) {
        sharefs.edit().putLong("total_money_in_banks", money).apply()
        totalMoneyInBanksLiveData.value = money
    }
    fun setTotalCash(money: Long) {
        sharefs.edit().putLong("total_cash", money).apply()
        totalCashLiveData.value = money
    }

    fun setMoneyUnit(moneyUnit: String) {
        sharefs.edit().putString("money_unit", moneyUnit).apply()
        moneyUnitLiveData.value = moneyUnit
    }

    fun setMoneyFormat(moneyFormat: String) {
        sharefs.edit().putString("money_format", moneyFormat).apply()
        moneyFormatLiveData.value = moneyFormat
    }

}
