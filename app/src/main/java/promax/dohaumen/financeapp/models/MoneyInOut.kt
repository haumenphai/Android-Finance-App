package promax.dohaumen.financeapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson


@Entity
class MoneyInOut: BaseModel {

    var type: MoneyInOutType = MoneyInOutType.IN
    var amount: Long = 0
    var currency: String = "cash" // cash, bank
    var desc: String = ""

    @Ignore
    var listTypeOfSpending: List<MoneyType> = listOf()
    var typeOfSpendingJson = ""
    var datetime: String = ""

    @Ignore
    var isChecked: Boolean = false
    var isDeleted: Boolean = false

    constructor()

    @Ignore
    constructor(
        name: String,
        type: MoneyInOutType,
        amount: Long,
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
        if (typeOfSpendingJson != "") {
            this.listTypeOfSpending = Gson().fromJson(this.typeOfSpendingJson, Array<MoneyType>::class.java).asList()
        }
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
            MoneyInOut("Buy car", MoneyInOutType.OUT, 10000, "Bank", datetime = "2022-12-12 20:20"),
            MoneyInOut("Sell car", MoneyInOutType.IN, 50000, "Cash", datetime = "2022-01-12 17:01")
        )
    }

    enum class MoneyInOutType { IN, OUT }
}
