fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        val map = getData(input)
        repeat(100) {
            map
                .iterateItems { i, j, _ ->
                    count += increase(map, i, j)
                }.resetZeros()
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        with(getData(input)) {
            while (true) {
                count++
                var light = 0
                iterateItems { i, j, _ ->
                    light += increase(this, i, j)
                }
                if (light == this.size * this[0].size) {
                    return count
                }
                resetZeros()
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)
    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

private fun List<MutableList<Int>>.resetZeros(): List<MutableList<Int>> {
    this.iterateItems { i, j, value ->
        if (value > 9) {
            this[i][j] = 0
        }
    }
    return this
}

private fun List<MutableList<Int>>.iterateItems(callback: (Int, Int, Int) -> Unit): List<MutableList<Int>> {
    this.forEachIndexed { x, list ->
        list.forEachIndexed { y, value ->
            callback(x, y, value)
        }
    }
    return this
}

private fun increase(map: List<MutableList<Int>>, x: Int, y: Int): Int {
    if (x < 0 || x >= map.size || y < 0 || y >= map[x].size) {
        return 0
    }
    map[x][y] = map[x][y] + 1
    return if (map[x][y] == 10) {
        1 + increase(map, x - 1, y - 1) +
                increase(map, x - 1, y) +
                increase(map, x - 1, y + 1) +
                increase(map, x, y - 1) +
                increase(map, x, y + 1) +
                increase(map, x + 1, y - 1) +
                increase(map, x + 1, y) +
                increase(map, x + 1, y + 1)
    } else {
        0
    }
}

private fun getData(input: List<String>): List<MutableList<Int>> {
    val map = mutableListOf<MutableList<Int>>()
    input.forEach { line ->
        map.add(line.map { it - '0' }.toMutableList())
    }
    return map
}