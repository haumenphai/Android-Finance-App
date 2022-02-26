package promax.dohaumen.financeapp.models

import promax.dohaumen.financeapp.MyApp
import promax.dohaumen.financeapp.R

class Currency {
    companion object {
        val BANK = MyApp.context.getString(R.string.bank)
        val CASH = MyApp.context.getString(R.string.cash)
    }
}
