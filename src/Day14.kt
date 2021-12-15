import java.lang.StringBuilder

fun main() {
    fun part1(input: List<String>): Long {
        return count(input, 10)
    }

    fun part2(input: List<String>): Long {
        return count(input, 40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

private fun count(input: List<String>, times: Int): Long {
    val map = mutableMapOf<String, String>()
    input.filter { it.contains("->") }
        .forEach {
            val list = it.split(" -> ")
            map[list[0]] = list[1]
        }
    var data = mutableMapOf<String, Long>()
    val raw = input[0]
    for (i in 0..raw.length - 2) {
        val key = "" + raw[i] + raw[i + 1]
        data[key] = data.getOrDefault(key, 0) + 1
    }
    repeat(times) {
        val newData = mutableMapOf<String, Long>()
        data.forEach { pair ->
            val start = pair.key[0].toString()
            val end = pair.key[1].toString()
            val middle = map["" + start + end].toString()
            listOf(start + middle, middle + end)
                .forEach {
                    newData[it] = newData.getOrDefault(it, 0) + pair.value
                }
        }
        data = newData
    }
    val count = mutableMapOf<Char, Long>()
    data.forEach {
        val first = it.key[0]
        count[first] = count.getOrDefault(first, 0) + it.value
    }
    val last = raw[raw.length - 1]
    count[last] = count.getOrDefault(last, 0) + 1
    return count.maxOf { it.value } - count.minOf { it.value }
}
