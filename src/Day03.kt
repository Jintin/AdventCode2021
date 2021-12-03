fun main() {
    fun part1(input: List<String>): Int {
        val record = IntArray(input[0].length)
        input.forEach {
            it.toCharArray().forEachIndexed { index, c ->
                if (c == '1') {
                    record[index]++
                } else {
                    record[index]--
                }
            }
        }
        var gamma = 0
        var epsilon = 0
        record.forEach {
            gamma *= 2
            epsilon *= 2
            if (it > 0) {
                gamma += 1
            } else {
                epsilon += 1
            }
        }
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val generator = filter(input, 0, '1', '0')
        val scrubber = filter(input, 0, '0', '1')
        return generator.toNumber() * scrubber.toNumber()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun String.toNumber(): Int {
    var value = 0
    this.forEach {
        value *= 2
        value += it - '0'
    }
    return value
}

fun filter(list: List<String>, index: Int, positiveChar: Char, negativeChar: Char): String {
    if (list.size == 1) return list[0]
    var count = 0
    list.forEach {
        if (it[index] == '1') {
            count++
        } else {
            count--
        }
    }
    val target = if (count >= 0) {
        positiveChar
    } else {
        negativeChar
    }
    val result = list.filter {
        it[index] == target
    }
    return filter(result, index + 1, positiveChar, negativeChar)
}
