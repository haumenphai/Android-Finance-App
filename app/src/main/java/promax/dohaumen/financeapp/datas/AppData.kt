package promax.dohaumen.financeapp.datas

import android.content.Context
import android.content.SharedPreferences
import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.helper.formatNumber

object AppData {
    private val sharefs: SharedPreferences =
        MyApp.context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    fun getTotalMoney() = sharefs.getLong("total_money", 0L)
    fun setTotalMoney(money: Long) = sharefs.edit().putLong("total_money", money).apply()

    fun getTotalMoneyInBanks() = sharefs.getLong("total_money_in_banks", 0L)
    fun setTotalMoneyInBanks(money: Long) = sharefs.edit().putLong("total_money_in_banks", money).apply()

    fun getTotalCash() = sharefs.getLong("total_cash", 0L)
    fun setTotalCash(money: Long) = sharefs.edit().putLong("total_cash", money).apply()

    fun getMoneyUnit() = sharefs.getString("money_unit", "đ")
    fun setMoneyUnit(moneyUnit: String) = sharefs.edit().putString("money_unit", moneyUnit).apply()

    fun getMoneyFormat() = sharefs.getString("money_format", ".")
    fun setMoneyFormat(moneyFormat: String) = sharefs.edit().putString("money_format", moneyFormat).apply()

    fun getTotalMoneyFormated() =
        "${getTotalMoney().toString().formatNumber(getMoneyFormat()!!)}  ${getMoneyUnit()}"
    fun getTotalMoneyInBanksFormated() =
        "${getTotalMoneyInBanks().toString().formatNumber(getMoneyFormat()!!)}  ${getMoneyUnit()}"
    fun getTotalCashFormated() =
        "${getTotalCash().toString().formatNumber(getMoneyFormat()!!)}  ${getMoneyUnit()}"
}
