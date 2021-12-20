fun main() {
    fun part1(input: List<String>) =
        count(input, 2)

    fun part2(input: List<String>) =
        count(input, 50)

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}

private fun List<List<Int>>.getValue(default: Int, digits: Int, x: Int, y: Int): Int {
    val bit = if (x in this.indices && y in this[x].indices) this[x][y] else default
    return bit shl digits
}

private fun transform(map: List<Int>, source: List<List<Int>>, default: Int): List<List<Int>> {
    val result = mutableListOf<MutableList<Int>>()
    for (i in -1..source.size) {
        val list = mutableListOf<Int>()
        for (j in -1..source[0].size) {
            val b = source.getValue(default, 8, i - 1, j - 1) +
                    source.getValue(default, 7, i - 1, j) +
                    source.getValue(default, 6, i - 1, j + 1) +
                    source.getValue(default, 5, i, j - 1) +
                    source.getValue(default, 4, i, j) +
                    source.getValue(default, 3, i, j + 1) +
                    source.getValue(default, 2, i + 1, j - 1) +
                    source.getValue(default, 1, i + 1, j) +
                    source.getValue(default, 0, i + 1, j + 1)
            list.add(map[b])
        }
        result.add(list)
    }
    return result
}

private fun count(input: List<String>, times: Int): Int {
    val map = input.first().map { if (it == '#') 1 else 0 }
    var output = input.drop(2).map { str ->
        str.map { if (it == '#') 1 else 0 }
    }
    var default = 0
    repeat(times) {
        output = transform(map, output, default)
        default = if (default == 0) map.first() else map.last()
    }
    return output.sumOf {
        it.count { value ->
            value == 1
        }
    }
}