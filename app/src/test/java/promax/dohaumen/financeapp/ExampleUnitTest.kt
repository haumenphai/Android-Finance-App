package promax.dohaumen.financeapp

import org.junit.Test

import org.junit.Assert.*

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

