package promax.dohaumen.financeapp.helper

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import promax.dohaumen.financeapp.MyApp

fun getStr(id: Int): String = MyApp.context.getString(id)

fun getColor(id: Int) = ContextCompat.getColor(MyApp.context, id)

fun TextView.setTextBold(vararg content: String) {
    val spannable = SpannableString(this.text)

    content.forEach {
        val index = this.text.toString().getIndex(it)
        spannable.setSpan(StyleSpan(Typeface.BOLD), index[0], index[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.text = spannable
}

fun TextView.setTextBold(listContent: List<String>) {
    listContent.forEach {
        this.setTextBold(it)
    }
}
