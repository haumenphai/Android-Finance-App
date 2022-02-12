package promax.dohaumen.financeapp.helper

import androidx.core.content.ContextCompat
import promax.dohaumen.financeapp.MyApp

fun getStr(id: Int) = MyApp.context.getString(id)

fun getColor(id: Int) = ContextCompat.getColor(MyApp.context, id)