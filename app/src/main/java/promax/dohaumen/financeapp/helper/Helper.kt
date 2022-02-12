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
    return result.reversed()
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTimeStr(format: String = "yyyy/MM/dd hh:mm"): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(Date())
}
