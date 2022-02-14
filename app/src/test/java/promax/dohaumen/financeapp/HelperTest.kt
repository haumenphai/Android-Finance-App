package promax.dohaumen.financeapp

import android.text.TextUtils
import androidx.room.util.StringUtil
import org.junit.Test
import org.junit.Assert.*
import promax.dohaumen.financeapp.helper.formatNumber
import promax.dohaumen.financeapp.helper.getCurrentTimeStr
import promax.dohaumen.financeapp.helper.isNumeric
import promax.dohaumen.financeapp.helper.to2Decimal
import java.math.BigDecimal
import java.text.DecimalFormat

class HelperTest {

    @Test
    fun testStringFormat() {


        assertEquals("100.000", "100000".formatNumber())
        assertEquals("100.000.000", "100000000".formatNumber())
        assertEquals("1.234.567", "1234567".formatNumber())
        assertEquals("-1.234.567", "-1234567".formatNumber())
        assertEquals("-100.234.567", "-100234567".formatNumber())

        assertEquals("1.234", "1234".formatNumber())
        assertEquals("123", "123".formatNumber())
        assertEquals("12", "12".formatNumber())
        assertEquals("1", "1".formatNumber())

        assertEquals("1 234 567", "1234567".formatNumber(' '))
        assertEquals("1,234,567", "1234567".formatNumber(','))
        assertEquals("123,456,000.91", "123456000.91".formatNumber(','))
        assertEquals("123.456.777,91", "123456777.91".formatNumber('.'))
        assertEquals("123 456 777.91", "123456777.91".formatNumber(' '))
    }

    @Test
    fun testGetCurrentTimeStr() {
        println(getCurrentTimeStr())
    }

}
