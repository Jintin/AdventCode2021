import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val list = input[0].split(",").map { it.toInt() }
        return calMinFuel(list) { it }
    }

    fun part2(input: List<String>): Int {
        val list = input[0].split(",").map { it.toInt() }
        val map = getFuelMap()
        return calMinFuel(list) {
            map[it] ?: 0
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

fun calMinFuel(list: List<Int>, fuelCounter: (Int) -> Int): Int {
    var min = Integer.MAX_VALUE
    for (center in 0..2000) {
        var count = 0
        list.forEach {
            count += fuelCounter(abs(it - center))
        }
        min = min(min, count)
    }

    return min
}

fun getFuelMap(): Map<Int, Int> {
    val map = mutableMapOf<Int, Int>()
    var value = 1
    for (i in 1..2000) {
        map[i] = value
        value += i + 1
    }
    return map
}
