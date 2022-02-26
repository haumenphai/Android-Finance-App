package promax.dohaumen.financeapp.models

import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R

class Currency {
    companion object {
//        val BANK = MyApp.context.getString(R.string.bank)
//        val CASH = MyApp.context.getString(R.string.cash)
        const val BANK = "bank"
        const val CASH = "cash"

        val BANK_I18N = MyApp.context.getString(R.string.bank)
        val CASH_I18N = MyApp.context.getString(R.string.cash)
    }
}
