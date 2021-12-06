
fun count(input: List<String>, round: Int): Long {
    var map = input[0].split(",")
        .groupingBy { it.toInt() }
        .eachCount()
        .mapValues { it.value.toLong() }
    for (i in 0 until round) {
        val newMap = mutableMapOf<Int, Long>()
        var count = 0L
        map.forEach {
            val newKey = if (it.key == 0) {
                count += it.value
                6
            } else {
                it.key - 1
            }
            newMap[newKey] = it.value + (newMap[newKey] ?: 0)
        }
        if (count != 0L) {
            newMap[8] = (newMap[8] ?: 0) + count
        }
        map = newMap
    }
    return map.values.sum()
}

fun main() {
    fun part1(input: List<String>): Long {
        return count(input, 80)
    }

    fun part2(input: List<String>): Long {
        return count(input, 256)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

