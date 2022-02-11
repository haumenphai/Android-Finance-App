package promax.dohaumen.financeapp.datas

import android.content.Context
import android.content.SharedPreferences
import promax.dohaumen.financeapp.MyApp

object AppData {
    private val sharefs: SharedPreferences =
        MyApp.context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    fun getTotalMoney() = sharefs.getString("total_money", "0")
    fun setTotalMoney(money: String) = sharefs.edit().putString("total_money", money).apply()

    fun getTotalMoneyInBanks() = sharefs.getString("total_money_in_banks", "")
    fun setTotalMoneyInBanks(money: String) = sharefs.edit().putString("total_money_in_banks", money).apply()

    fun getTotalCash() = sharefs.getString("total_cash", "")
    fun setTotalCash(money: String) = sharefs.edit().putString("total_cash", money).apply()
}
