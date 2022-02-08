package promax.dohaumen.financeapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
class MoneyInOut {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var type: MoneyInOutType = MoneyInOutType.IN
    var amount: Double = 0.0
    var desc: String = ""
    var typeOfSpending: String = ""
    var datetime: String = ""

    @Ignore
    var isChecked: Boolean = false
    var isDeleted: Boolean = false

    constructor()

    @Ignore
    constructor(
        name: String,
        type: MoneyInOutType,
        amount: Double,
        desc: String = "",
        typeOfSpending: String = "",
        datetime: String = ""
    ) {
        this.name = name
        this.type = type
        this.amount = amount
        this.desc = desc
        this.typeOfSpending = typeOfSpending
        this.datetime = datetime
    }

    companion object {
        fun getDemoMoneyInOut() = listOf(
            MoneyInOut("Buy car", MoneyInOutType.OUT, 10000.0, datetime = "2022-12-12 20:20"),
            MoneyInOut("Sell car", MoneyInOutType.IN, 50000.0, datetime = "2022-01-12 17:01")
        )
    }

    enum class MoneyInOutType { IN, OUT }
}

