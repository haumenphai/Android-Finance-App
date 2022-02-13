package promax.dohaumen.financeapp.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

fun String.formatNumber(charFormat: String = "."): String {
    var result = ""

    var i = 0
    for (c in this.reversed().toCharArray()) {
        i += 1
        result += c
        if (i % 3 == 0 && i != this.length) {
            result += charFormat
        }
    }
    result = result.reversed()
    if (result[0] == '-' && result.replace("-", "").startsWith(charFormat)) {
        result = result.replaceFirst(charFormat, "")
    }
    return result
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTimeStr(format: String = "yyyy-MM-dd HH:mm"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
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
