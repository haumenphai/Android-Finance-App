package promax.dohaumen.financeapp.helper

import android.annotation.SuppressLint
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.formatNumber(charFormat: Char = '.'): String {
    val number = this
    if (!number.isNumeric()) return this

    var result: String
    if (this.contains(".")) {
        result = DecimalFormat("#,###.00").format(BigDecimal(number))
    } else {
        result = DecimalFormat("#,###").format(BigDecimal(number))
    }

    result = result.replace(",", "*")
    if (charFormat == '.') {
        result = result.replace(".", ",")
    }
    result = result.replace("*", charFormat.toString())
    return result
}

fun String.to2Decimal(): String {
    if (this.endsWith(".00")) {
        return this.substring(0, this.indexOf(".00"))
    }
    return this
}

fun String.isNumeric(): Boolean {
    return this.matches(Regex("-?\\d+(\\.\\d+)?")) //match a number with optional '-' and decimal.
}


@SuppressLint("SimpleDateFormat")
fun getCurrentTimeStr(format: String = "yyyy-MM-dd HH:mm"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
}

fun <K, V> Map<K, V>.getKey(value: V): K? {
    for ((key, value1) in this) {
        if (Objects.equals(value, value1)) {
            return key
        }
    }
    return null
}


@SuppressLint("SimpleDateFormat")
class DateTime {
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0
    var seconds = 0
    var milisecond = 0

    override fun toString(): String =
        "Datetime(year=$year, month=$month, day=$day, hour=$hour, minute=$minute, second=$seconds, milisecond=$milisecond)"

    fun format(format: String = "yyyy-MM-dd HH:mm:ss [E]"): String {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, seconds)

        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(c.time)
    }

    fun toMiliseconds(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, seconds)

        return c.timeInMillis
    }
}
