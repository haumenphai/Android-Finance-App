package promax.dohaumen.financeapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson


@Entity
class MoneyInOut: BaseModel {

    var type: MoneyInOutType = MoneyInOutType.IN
    var amount: String = "0"         // support BigDecimal
    var currency: String = Currency.CASH    // cash, bank
    var desc: String = ""

    @Ignore
    var listTypeOfSpending: List<MoneyType> = listOf()
    var typeOfSpendingJson = ""
    var datetime: String = ""

    @Ignore
    var isChecked: Boolean = false
    var isDeleted: Boolean = false
    var computeIntoTheTotalMoney = true

    constructor()

    @Ignore
    constructor(
        name: String,
        type: MoneyInOutType,
        amount: String,
        currency: String,
        desc: String = "",
        typeOfSpendingJson: String = "",
        datetime: String = "",
        computeIntoTheTotalMoney: Boolean = true
    ) {
        this.name = name
        this.type = type
        this.amount = amount
        this.currency = currency
        this.desc = desc
        this.typeOfSpendingJson = typeOfSpendingJson
        this.datetime = datetime
        this.computeIntoTheTotalMoney = computeIntoTheTotalMoney
        if (typeOfSpendingJson != "") {
            this.listTypeOfSpending = Gson().fromJson(this.typeOfSpendingJson, Array<MoneyType>::class.java).asList()
        }
    }

    fun setlistTypeOfSpending(list: List<MoneyType>) {
        this.listTypeOfSpending = list
        this.typeOfSpendingJson = list.toJson()
    }

    fun copy(): MoneyInOut {
        return MoneyInOut(
            this.name,
            this.type,
            this.amount,
            this.currency,
            this.desc,
            this.typeOfSpendingJson,
            this.datetime,
            this.computeIntoTheTotalMoney
        )
    }

    fun getListMoneyType(): List<MoneyType> {
        var list = listOf<MoneyType>()
        if (this.listTypeOfSpending.isNotEmpty()) {
            return this.listTypeOfSpending
        }
        if (this.typeOfSpendingJson != "") {
           list = Gson().fromJson(this.typeOfSpendingJson, Array<MoneyType>::class.java).asList()
        }
        return list
    }

    companion object {
        fun getDemoMoneyInOut() = listOf(
            MoneyInOut("Buy car", MoneyInOutType.OUT, "10000", Currency.BANK, datetime = "2022-12-12 20:20"),
            MoneyInOut("Sell car", MoneyInOutType.IN, "50000", Currency.CASH, datetime = "2022-01-12 17:01")
        )

        fun getBigListDemo(count: Int=10000): MutableList<MoneyInOut> {
            val list = mutableListOf<MoneyInOut>()
            for (i in 0..count) {
                val moneyIO = MoneyInOut(
                    "${i+1}",
                    MoneyInOutType.IN,
                    "10000",
                    Currency.BANK,
                    datetime = "2022-12-12 20:20",
                    desc = "gg",
                    typeOfSpendingJson = listOf(MoneyType("Test1", MoneyInOutType.IN)).toJson()
                )
                if (i % 2 == 0) {
                    moneyIO.type = MoneyInOutType.IN
                } else {
                    moneyIO.type = MoneyInOutType.OUT
                }
                list.add(moneyIO)
            }
            return list
        }

    }

    fun getSearchableFields(): List<String> {
        return listOf("name", "amount", "currency", "type", "desc", "listTypeOfSpending",
            "datetime", "computeIntoTheTotalMoney")
    }

    enum class MoneyInOutType { IN, OUT }
}
