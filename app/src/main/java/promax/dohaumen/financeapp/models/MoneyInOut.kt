package promax.dohaumen.financeapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson


@Entity
class MoneyInOut {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var type: MoneyInOutType = MoneyInOutType.IN
    var amount: Double = 0.0
    var currency: String = "cash" // cash, bank
    var desc: String = ""

    @Ignore
    var listTypeOfSpending: List<MoneyType> = listOf()
    var typeOfSpendingJson = ""
    var datetime: String = ""

    @Ignore
    var isChecked: Boolean = false
    var isDeleted: Boolean = false

    constructor() {
        if (typeOfSpendingJson != "") {
            this.listTypeOfSpending = Gson().fromJson(this.typeOfSpendingJson, Array<MoneyType>::class.java).asList()
        }
    }

    @Ignore
    constructor(
        name: String,
        type: MoneyInOutType,
        amount: Double,
        currency: String,
        desc: String = "",
        typeOfSpendingJson: String = "",
        datetime: String = ""
    ) {
        this.name = name
        this.type = type
        this.amount = amount
        this.currency = currency
        this.desc = desc
        this.typeOfSpendingJson = typeOfSpendingJson
        this.datetime = datetime
    }

    @JvmName("setListTypeOfSpending1")
    fun setListTypeOfSpending(list: List<MoneyType>) {
        val gson = Gson()
        this.typeOfSpendingJson = gson.toJson(list)
        this.listTypeOfSpending = list
    }

    companion object {
        fun getDemoMoneyInOut() = listOf(
            MoneyInOut("Buy car", MoneyInOutType.OUT, 10000.0, "Bank", datetime = "2022-12-12 20:20"),
            MoneyInOut("Sell car", MoneyInOutType.IN, 50000.0, "Cash", datetime = "2022-01-12 17:01")
        )
    }

    enum class MoneyInOutType { IN, OUT }
}

