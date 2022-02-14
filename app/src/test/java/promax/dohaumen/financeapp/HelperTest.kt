package promax.dohaumen.financeapp

import org.junit.Test
import org.junit.Assert.*
import promax.dohaumen.financeapp.helper.formatNumber
import promax.dohaumen.financeapp.helper.getCurrentTimeStr
import java.math.BigDecimal

class HelperTest {

    @Test
    fun testStringFormat() {
//        println(Long.MAX_VALUE.toString().length)
        println(Double.MAX_VALUE.toString().length)
        println(BigDecimal("123456789123456789.99") + BigDecimal("1.01"))
        println(5.hashCode())

        assertEquals("100.000", "100000".formatNumber())
        assertEquals("100.000.000", "100000000".formatNumber())
        assertEquals("1.234.567", "1234567".formatNumber())
        assertEquals("-1.234.567", "-1234567".formatNumber())
        assertEquals("-100.234.567", "-100234567".formatNumber())

        assertEquals("1.234", "1234".formatNumber())
        assertEquals("123", "123".formatNumber())
        assertEquals("12", "12".formatNumber())
        assertEquals("1", "1".formatNumber())

        assertEquals("1 234 567", "1234567".formatNumber(" "))
        assertEquals("1,234,567", "1234567".formatNumber(","))
    }

    @Test
    fun testGetCurrentTimeStr() {
        println(getCurrentTimeStr())
    }

}
