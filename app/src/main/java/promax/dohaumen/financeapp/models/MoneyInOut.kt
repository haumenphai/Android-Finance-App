package promax.dohaumen.financeapp.models

import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.Gson
import org.jetbrains.annotations.TestOnly
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.helper.*
import java.lang.Exception
import java.math.BigDecimal


@Entity
class MoneyInOut: BaseModel {

    var type: MoneyInOutType = MoneyInOutType.IN // money type
    var amount: String = "0"         // support BigDecimal
    var currency: String = Currency.CASH    // cash, bank
    var desc: String = ""

    @Ignore
    var listTypeOfSpending: List<MoneyType> = listOf()
    var typeOfSpendingJson = "" // type
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

    fun getValueOfFieldString(field: String): String {
        when (field) {
            "name" -> return this.name
            "type" -> return this.type.toString()
            "amount" -> return this.amount
            "currency" -> return this.currency
            "desc" -> return this.desc
            "datetime" -> return this.datetime
        }
        throw Exception("field invalid!")
    }

    fun getValueOfFieldNumber(field: String): Double {
        when (field) {
            "amount" -> return this.amount.toDouble()
            "id" -> return this.id.toDouble()
        }
        throw Exception("field invalid!")
    }

    fun getDateTime(): DateTime {
        val s = datetime.split(" ")
        val year = s[0].split("-")[0].toInt()
        val month = s[0].split("-")[1].toInt()
        val day = s[0].split("-")[2].toInt()

        var hour = 0
        var minute = 0

        if (s.size == 2) {
            hour = s[1].split(":")[0].toInt()
            minute = s[1].split(":")[1].toInt()
        }
        return DateTime(
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute
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
            MoneyInOut("Buy car", MoneyInOutType.OUT, "10000", Currency.BANK,
                datetime = "2022-12-12 20:20",
                computeIntoTheTotalMoney = false
            ),
            MoneyInOut("Sell car", MoneyInOutType.IN, "50000", Currency.CASH,
                datetime = "2022-01-12 17:01",
                computeIntoTheTotalMoney = false
            )
        )

        @TestOnly
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

        @TestOnly
        fun getListDemoTest() = listOf(
            MoneyInOut("Buy car (Today)", MoneyInOutType.OUT, "20000", Currency.BANK,
                datetime = getCurrentTimeStr("yyyy-MM-dd hh:mm")
            ),
            MoneyInOut("Sell something (Today)", MoneyInOutType.IN, "150", Currency.CASH,
                datetime = getCurrentTimeStr("yyyy-MM-dd")
            ),
            MoneyInOut("Buy carry (Yesterday)", MoneyInOutType.OUT, "10", Currency.CASH,
                datetime = getCurrentDatetime().minuteTime(day = 1).format("yyyy-MM-dd")
            ),
            MoneyInOut("Buy something (Last week)", MoneyInOutType.OUT, "100", Currency.CASH,
                datetime = getCurrentDatetime().minuteTime(day = 7).format("yyyy-MM-dd")
            ),
            MoneyInOut("Buy book: (Last month)", MoneyInOutType.OUT, "20", Currency.CASH,
                datetime = getCurrentDatetime().minuteTime(month = 1).format("yyyy-MM-dd")
            ),
            MoneyInOut("Sell car (Last year)", MoneyInOutType.IN, "10000", Currency.BANK,
                datetime = getCurrentDatetime().minuteTime(year = 1).format("yyyy-MM-dd HH:mm")
            ),


        )

    }

    enum class MoneyInOutType { IN, OUT }
}

fun List<MoneyInOut>.sumMoneyIn(): String {
    var totalMoneyIn = BigDecimal("0")
    this.filter { it.type == MoneyInOut.MoneyInOutType.IN }.forEach {
        totalMoneyIn += BigDecimal(it.amount)
    }
    return totalMoneyIn.toPlainString()
}

fun List<MoneyInOut>.sumMoneyOut(): String {
    var totalMoneyOut = BigDecimal("0")
    this.filter { it.type == MoneyInOut.MoneyInOutType.OUT }.forEach {
        totalMoneyOut += BigDecimal(it.amount)
    }
    return totalMoneyOut.toPlainString()
}

fun List<MoneyInOut>.sumCashIn(): String {
    var totalCashIn = BigDecimal("0")
    this.filter { it.type == MoneyInOut.MoneyInOutType.IN && it.currency == Currency.CASH }.forEach {
        totalCashIn += BigDecimal(it.amount)
    }
    return totalCashIn.toPlainString()
}

fun List<MoneyInOut>.sumCashOut(): String {
    var totalCashOut = BigDecimal("0")
    this.filter { it.type == MoneyInOut.MoneyInOutType.OUT && it.currency == Currency.CASH }.forEach {
        totalCashOut += BigDecimal(it.amount)
    }
    return totalCashOut.toPlainString()
}

fun List<MoneyInOut>.sumMoneyBankIn(): String {
    var totalMoneyBankIn = BigDecimal("0")
    this.filter { it.type == MoneyInOut.MoneyInOutType.IN && it.currency == Currency.BANK }.forEach {
        totalMoneyBankIn += BigDecimal(it.amount)
    }
    return totalMoneyBankIn.toPlainString()
}

fun List<MoneyInOut>.sumMoneyBankOut(): String {
    var totalMoneyBankOut = BigDecimal("0")
    this.filter { it.type == MoneyInOut.MoneyInOutType.OUT && it.currency == Currency.BANK }.forEach {
        totalMoneyBankOut += BigDecimal(it.amount)
    }
    return totalMoneyBankOut.toPlainString()
}

fun List<MoneyInOut>.moneyInCount(): Int {
    return this.filter { it.type == MoneyInOut.MoneyInOutType.IN }.size
}

fun List<MoneyInOut>.moneyOutCount(): Int {
    return this.filter { it.type == MoneyInOut.MoneyInOutType.OUT }.size
}

fun List<MoneyInOut>.cashInCount(): Int {
    return this.filter { it.type == MoneyInOut.MoneyInOutType.IN && it.currency == Currency.CASH }.size
}

fun List<MoneyInOut>.cashOutCount(): Int =
    this.filter { it.type == MoneyInOut.MoneyInOutType.OUT && it.currency == Currency.CASH }.size

fun List<MoneyInOut>.bankInCount(): Int =
    this.filter { it.type == MoneyInOut.MoneyInOutType.IN && it.currency == Currency.BANK }.size

fun List<MoneyInOut>.bankOutCount(): Int =
    this.filter { it.type == MoneyInOut.MoneyInOutType.OUT && it.currency == Currency.BANK }.size


/**
 * [
 *  1: moneyIO
 *       name: buy car
 *       amount: 20
 *       type: [buy xx]
 *
 *  2: moneyIO
 *       name: buy xxx
 *       amount: 20
 *       type: [buy xx]
 *
 *  3: moneyIO
 *       name: sell xxx
 *       amount: 10
 *       type: [sell xx]
 * ]
 */

/**
 * [
 *  {name: 'buy xx', 'amount': '+40 d', 'count': 2},
 *  {name: 'sell xx', 'amount': '-10 d', 'count': 1}
 * ]
 */
fun List<MoneyInOut>.getMoneyByType(): List<Map<String, String>> {
    val result = mutableListOf<Map<String, String>>()
    val typeNull1 = MoneyType(getStr(R.string.null1), MoneyInOut.MoneyInOutType.IN, id=0)
    val typeNull2 = MoneyType(getStr(R.string.null2), MoneyInOut.MoneyInOutType.OUT, id=1)


    val setType = mutableSetOf<MoneyType>()
    this.forEach {
        it.getListMoneyType().forEach {
            setType.add(it)
        }
        if (it.getListMoneyType().isEmpty()) {
            if (it.type == MoneyInOut.MoneyInOutType.IN) {
                setType.add(typeNull1)
                it.listTypeOfSpending = listOf(typeNull1)
            } else {
                it.listTypeOfSpending = listOf(typeNull2)
                setType.add(typeNull2)
            }
        }
    }

    setType.forEach { moneyType1 ->
        val map = mutableMapOf<String,String>()

        map["name"] = moneyType1.name

        var money = BigDecimal("0")
        var count = 0
        this.forEach { moneyIO ->
            moneyIO.getListMoneyType().forEach {  moneyType2 ->
                if (moneyType1.name == moneyType2.name) {
                    money += BigDecimal(moneyIO.amount)
                    count += 1
                }
            }
        }

        if (moneyType1.type == MoneyInOut.MoneyInOutType.OUT) {
            map["amount"] = "-${AppData.formatMoneyWithAppConfig(money.toPlainString())}"
        } else {
            map["amount"] = "+${AppData.formatMoneyWithAppConfig(money.toPlainString())}"
        }

        map["count"] = count.toString()
        result.add(map)
    }

    val mapNull1 = result.filter { it["name"] == getStr(R.string.null1) }
    val mapNull2 = result.filter { it["name"] == getStr(R.string.null2) }
    if (mapNull1.isNotEmpty()) {
        result.remove(mapNull1[0])
        result.add(mapNull1[0])
    }
    if (mapNull2.isNotEmpty()) {
        result.remove(mapNull2[0])
        result.add(mapNull2[0])
    }

    this.forEach {
        if (it.listTypeOfSpending.size == 1 &&
            (it.listTypeOfSpending[0] == typeNull1 || it.listTypeOfSpending[0] == typeNull2)) {
            it.listTypeOfSpending = listOf()
        }
    }


    return result
}
