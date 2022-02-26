package promax.dohaumen.financeapp

import android.text.TextUtils
import androidx.room.util.StringUtil
import org.junit.Test
import org.junit.Assert.*
import promax.dohaumen.financeapp.helper.*
import java.math.BigDecimal
import java.text.DecimalFormat

class HelperTest {

    @Test
    fun testFormatNumber() {
        assertEquals("100,000", "100000".formatNumber())
        assertEquals("100,000,000", "100000000".formatNumber())
        assertEquals("1,234,567", "1234567".formatNumber())
        assertEquals("-1,234,567", "-1234567".formatNumber())
        assertEquals("-100,234,567", "-100234567".formatNumber())
        assertEquals("-100,234,567.98", "-100234567.98".formatNumber())
    }

    @Test
    fun testGetCurrentTimeStr() {
        println(getCurrentTimeStr())
    }

    @Test
    fun testMinuteDateTime() {
        val t1 = DateTime(2022, 2, 23)
        assertEquals(t1.minuteTime(day = 1), DateTime(2022, 2, 22))
        assertEquals(t1.minuteTime(day = 7), DateTime(2022, 2, 16))
        assertEquals(t1.minuteTime(month = 1), DateTime(2022, 1, 23))
        assertEquals(t1.minuteTime(year = 1), DateTime(2021, 2, 23))

        val t2 = DateTime(2022, 2, 23, 20, 50, 30, 500)
        assertEquals(
            t2.minuteTime(1, 1, 1, 1, 1, 1, 1),
            DateTime(2021, 1, 22, 19, 49, 29, 499)
        )
    }

    @Test
    fun testAddDateTime() {
        val t1 = DateTime(2022, 2, 23)
        assertEquals(DateTime(2022, 2, 24), t1.addTime(day = 1))
        assertEquals(DateTime(2022, 3, 2), t1.addTime(day = 7))
        assertEquals(DateTime(2022, 3, 23), t1.addTime(month = 1))
        assertEquals(DateTime(2023, 2, 23), t1.addTime(year = 1))

        val t2 = DateTime(2022, 2, 23, 20, 50, 50, 500)
        assertEquals(
            DateTime(2023, 3, 24, 21, 51, 51, 501),
            t2.addTime(1,1,1,1,1,1,1)
        )
    }
}
