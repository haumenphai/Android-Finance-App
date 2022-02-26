package promax.dohaumen.financeapp.helper

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.Normalizer
import java.util.ArrayList
import java.util.regex.Pattern

fun String.formatNumber(): String {
    val number = this
    if (!number.isNumeric()) return this

    if (this.contains(".")) {
        return DecimalFormat("#,###.00").format(BigDecimal(number))
    }
    return DecimalFormat("#,###").format(BigDecimal(number))
}

fun String.removeAccents(): String {
    var s = this
    s = Normalizer.normalize(s, Normalizer.Form.NFD)
    s = s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
    s = s.replace("Đ", "D")
    s = s.replace("đ", "d")
    return s
}

fun String.removeRegex(): String {
    var s = this
    s = s.replace("\\", "\\\\")
    s = s.replace(".", "\\.")

    s = s.replace("[", "\\[")
    s = s.replace("]", "\\]")

    s = s.replace("(", "\\(")
    s = s.replace(")", "\\)")

    s = s.replace("*", "\\*")

    s = s.replace("{", "\\{")
    s = s.replace("}", "\\}")

    s = s.replace("+", "\\+")

    return s
}

fun String.getIndex(search: String, isRegex: Boolean = false): List<Int> {
    var _search = search
    val list: MutableList<Int> = ArrayList()
    if (!isRegex) _search = search.removeRegex()
    val pattern = Pattern.compile(_search)
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        val startIndex = matcher.start()
        val endIndex = matcher.end()
        list.add(startIndex)
        list.add(endIndex)
    }
    return list
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
