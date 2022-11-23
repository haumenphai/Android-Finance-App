package promax.dohaumen.financeapp

import org.junit.Test
import promax.dohaumen.financeapp.helper.DateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
        val s = listOf(Dog("g1", 5), Dog("g2", 5))
        val s1 = listOf(Dog("g1", 5))
        (s subtract s1).forEach {
            println(it.name)}
        println(s subtract s1)

        println('g')
    }

    @Test
    fun test2() {
//        val l1 = listOf("100", "2", "9", "30")
//        println(l1.sortedWith(compareBy({it.toDouble()}, {it.toDouble()})))

//        val yesterday = DateTime(2022, 2, 28)
//        val today = DateTime(2022, 3, 1)
//        println(yesterday.toMiliseconds() / 1000)
//        println(today.toMiliseconds() / 1000 - 24 * 60 * 60)
//
//
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.YEAR, 2022)
//        cal.set(Calendar.MONTH, 2)
//        cal.set(Calendar.DATE, 1)
//
//        cal.add(Calendar.DATE, -1)
//        println(dateFormat.format(cal.getTime()))
//


        val d1 = DateTime(2022, 1, 2).getLastMonth()
        val d2 = DateTime(2021, 11, 31)
        println(d1.equalsYM(d2))
//        println(d2.getWeekOfYear())
    }
}

class Dog {
    var name: String = ""
    var age: Int = 0


    constructor(name: String, age: Int) {
        this.name = name
        this.age= age
    }

    override fun equals(other: Any?): Boolean {
        if (other is Dog) {
            return this.name == other.name && this.age == age

        }
        return super.equals(other)

    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age
        return result
    }

//    override fun hashCode(): Int {
//        var result = name.hashCode()
//        result = 31 * result + age
//        return result
//    }

}

