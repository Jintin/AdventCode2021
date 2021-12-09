fun main() {
    fun part1(input: List<String>): Int {
        val map = mutableListOf<List<Int>>()

        input.forEach {
            val list = mutableListOf<Int>()
            it.forEach {
                list.add(it - '0')
            }
            map.add(list)
        }
        return countLow(map)
    }

    fun part2(input: List<String>): Int {
        val map = mutableListOf<List<Int>>()
        val checked = mutableListOf<MutableList<Boolean>>()
        input.forEach {
            val list = mutableListOf<Int>()
            val list2 = mutableListOf<Boolean>()
            it.forEach {
                list.add(it - '0')
                list2.add(it == '9')
            }
            map.add(list)
            checked.add(list2)
        }
        return countFlow(map, checked)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private fun findLowPoint(map: List<List<Int>>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    for (i in map.indices) {
        val list = map[i]
        for (j in list.indices) {
            val me = map[i][j]
            if ((i > 0 && me >= map[i - 1][j]) ||
                (i != map.size - 1 && me >= map[i + 1][j]) ||
                (j > 0 && me >= map[i][j - 1]) ||
                (j != map[i].size - 1 && me >= map[i][j + 1])
            ) {
                continue
            }
            result.add(Pair(i, j))
        }
    }
    return result
}

private fun countLow(map: List<List<Int>>): Int {
    var count = 0
    findLowPoint(map).forEach {
        count += map[it.first][it.second] + 1
    }
    return count
}

private fun countFlow(map: List<List<Int>>, checked: List<MutableList<Boolean>>): Int {
    val rankList = mutableListOf<Int>()
    findLowPoint(map).forEach {
        val value = checkGroupSize(it.first, it.second, map, checked)
        rankList.add(value)
    }
    rankList.sort()
    val size = rankList.size
    return rankList[size - 1] * rankList[size - 2] * rankList[size - 3]
}

private fun checkGroupSize(x: Int, y: Int, map: List<List<Int>>, checked: List<MutableList<Boolean>>): Int {
    if (checked[x][y]) {
        return 0
    }
    checked[x][y] = true
    val me = map[x][y]

    var count = 1

    if (x > 0 && me < map[x - 1][y]) {
        count += checkGroupSize(x - 1, y, map, checked)
    }
    if (x != map.size - 1 && me < map[x + 1][y]) {
        count += checkGroupSize(x + 1, y, map, checked)
    }
    if (y > 0 && me < map[x][y - 1]) {
        count += checkGroupSize(x, y - 1, map, checked)
    }
    if (y != map[x].size - 1 && me < map[x][y + 1]) {
        count += checkGroupSize(x, y + 1, map, checked)
    }
    return count
}
