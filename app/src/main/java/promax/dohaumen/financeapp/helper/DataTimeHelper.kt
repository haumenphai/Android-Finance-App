package promax.dohaumen.financeapp.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getCurrentTimeStr(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
}

fun getCurrentDatetime(): DateTime {
    val dateStr = getCurrentTimeStr()

    val s = dateStr.split(" ")
    val year = s[0].split("-")[0].toInt()
    val month = s[0].split("-")[1].toInt()
    val day = s[0].split("-")[2].toInt()


    val hour = s[1].split(":")[0].toInt()
    val minute = s[1].split(":")[1].toInt()
    val seconds = s[1].split(":")[2].toInt()
    return DateTime(
        year = year,
        month = month,
        day = day,
        hour = hour,
        minute = minute,
        seconds = seconds
    )
}

fun Calendar.toDatetime(): DateTime {
    return DateTime(
        year = this.get(Calendar.YEAR),
        month = this.get(Calendar.MONTH) + 1,
        day = this.get(Calendar.DAY_OF_MONTH),
        hour = this.get(Calendar.HOUR_OF_DAY),
        minute = this.get(Calendar.MINUTE),
        seconds = this.get(Calendar.SECOND),
        milisecond = this.get(Calendar.MILLISECOND)
    )
}

fun String.toYMD(): String {
    if (this.isEmpty()) return this

    // input: yyyy-MM-dd xxxx
    val date = this.split(" ")[0]
    val y = date.split("-")[0]
    val m = date.split("-")[1]
    val d = date.split("-")[2]
    return "$y-$m-$d"
}

@SuppressLint("SimpleDateFormat")
class DateTime {
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0
    var seconds = 0
    var millisecond = 0

    constructor()

    constructor(
        year: Int = 0, month: Int = 0,
        day: Int = 0, hour: Int = 0, minute: Int = 0, seconds: Int = 0, milisecond: Int = 0
    ) {
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
        this.seconds = seconds
        this.millisecond = milisecond
    }

    override fun toString(): String =
        "Datetime(year=$year, month=$month, day=$day, hour=$hour, minute=$minute, second=$seconds, milisecond=$millisecond)"

    fun format(format: String = "yyyy-MM-dd HH:mm:ss [E]"): String {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month - 1)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, seconds)

        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(c.time)
    }

    fun addTime(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0,
        hour: Int = 0,
        minute: Int = 0,
        seconds: Int = 0,
        milisecond: Int = 0
    ): DateTime {
        val c = getCalendar()
        c.add(Calendar.YEAR, year)
        c.add(Calendar.MONTH, month)
        c.add(Calendar.DAY_OF_MONTH, day)
        c.add(Calendar.HOUR_OF_DAY, hour)
        c.add(Calendar.MINUTE, minute)
        c.add(Calendar.SECOND, seconds)
        c.add(Calendar.MILLISECOND, milisecond)
        return c.toDatetime()
    }

    fun minuteTime(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0,
        hour: Int = 0,
        minute: Int = 0,
        seconds: Int = 0,
        millisecond: Int = 0
    ): DateTime {
        val c = getCalendar()
        c.add(Calendar.YEAR, -year)
        c.add(Calendar.MONTH, -month)
        c.add(Calendar.DAY_OF_MONTH, -day)
        c.add(Calendar.HOUR_OF_DAY, -hour)
        c.add(Calendar.MINUTE, -minute)
        c.add(Calendar.SECOND, -seconds)
        c.add(Calendar.MILLISECOND, -millisecond)
        return c.toDatetime()
    }


    fun getCalendar(): Calendar {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month - 1)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, seconds)
        c.set(Calendar.MILLISECOND, millisecond)
        return c
    }

    fun getYesterday(): DateTime {
        val c = getCalendar()
        c.add(Calendar.DATE, -1)
        return DateTime(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getLastWeek(): DateTime {
        val c = getCalendar()
        c.add(Calendar.WEEK_OF_MONTH, -1)
        return DateTime(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getLastMonth(): DateTime {
        val c = getCalendar()
        c.add(Calendar.MONTH, -1)
        return DateTime(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
    }


    fun toMiliseconds(): Long {
        return getCalendar().timeInMillis
    }

    fun getWeekOfYear(): Int {
        return getCalendar().get(Calendar.WEEK_OF_YEAR)
    }

    override fun equals(other: Any?): Boolean {
        if (other is DateTime) {
            return year == other.year &&
                    month == other.month &&
                    day == other.day &&
                    hour == other.hour &&
                    minute == other.minute &&
                    seconds == other.seconds &&
                    millisecond == other.millisecond
        }
        return super.equals(other)
    }

    fun equalsYMD(other: DateTime): Boolean {
        return year == other.year &&
                month == other.month &&
                day == other.day
    }

    fun equalsYM(other: DateTime): Boolean {
        return year == other.year && month == other.month
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + day
        result = 31 * result + hour
        result = 31 * result + minute
        result = 31 * result + seconds
        result = 31 * result + millisecond
        return result
    }
}
