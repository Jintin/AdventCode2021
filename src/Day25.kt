fun main() {
    fun part1(input: List<String>): Int {
        val map = Array(input.size) { Array(input[0].length) { 0 } }
        input.forEachIndexed { i, s ->
            s.forEachIndexed { j, c ->
                map[i][j] = when (c) {
                    '.' -> 0
                    '>' -> 1
                    'v' -> 2
                    else -> throw RuntimeException()
                }
            }
        }
        var count = 0
        while (true) {
            count++
            var change = false
            map.forEach { list ->
                val indexes = mutableSetOf<Int>()
                list.forEachIndexed { j, value ->
                    if (value == 1 && list[(j + 1) % list.size] == 0) {
                        indexes.add(j)
                    }
                }
                if (indexes.size > 0) {
                    change = true
                    indexes.forEach {
                        list[it] = 0
                        list[(it + 1) % list.size] = 1
                    }
                }
            }
            map[0].indices.forEach { j ->
                val indexes = mutableSetOf<Int>()
                map.indices.forEach { i ->
                    if (map[i][j] == 2 && map[(i + 1) % map.size][j] == 0) {
                        indexes.add(i)
                    }
                }
                if (indexes.size > 0) {
                    change = true
                    indexes.forEach {
                        map[it][j] = 0
                        map[(it + 1) % map.size][j] = 2
                    }
                }
            }
            if (!change) {
                break
            }
        }
        return count
    }

    val input = readInput("Day25")
    println(part1(input))
}
