import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val score = getScore(input)
        return findPath(score)
    }

    fun part2(input: List<String>): Int {
        val score = times5(getScore(input))
        return findPath(score)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

private fun getScore(input: List<String>): List<List<Int>> {
    return input.map { str ->
        str.map { it - '0' }
    }
}

private fun times5(score: List<List<Int>>): List<List<Int>> {
    val resultScore = mutableListOf<MutableList<Int>>()
    repeat(5) { i ->
        score.map { list ->
            val result = mutableListOf<Int>()
            repeat(5) { j ->
                list.map {
                    var value = it + i + j
                    while (value >= 10) {
                        value -= 9
                    }
                    result.add(value)
                }
            }
            resultScore.add(result)
        }
    }
    return resultScore
}

private fun findPath(score: List<List<Int>>): Int {
    val sum = Array(score.size) {
        Array(score[it].size) {
            Int.MAX_VALUE
        }
    }
    val q = PriorityQueue { p1: Pair<Int, Int>, p2: Pair<Int, Int> ->
        sum[p1.first][p1.second] - sum[p2.first][p2.second]
    }
    sum[0][0] = 0
    q.offer(Pair(0, 0))
    while (q.isNotEmpty()) {
        val (x, y) = q.poll()
        val current = sum[x][y]
        record(x - 1, y, score, sum, current, q)
        record(x + 1, y, score, sum, current, q)
        record(x, y - 1, score, sum, current, q)
        record(x, y + 1, score, sum, current, q)
    }
    return sum[sum.size - 1][sum[0].size - 1]
}

private fun record(
    x: Int,
    y: Int,
    score: List<List<Int>>,
    sum: Array<Array<Int>>,
    current: Int,
    q: Queue<Pair<Int, Int>>
) {
    if (x >= score[0].size || y >= score.size || x < 0 || y < 0) {
        return
    }
    if (sum[x][y] > current + score[x][y]) {
        sum[x][y] = current + score[x][y]
        q.offer(Pair(x, y))
    }
}