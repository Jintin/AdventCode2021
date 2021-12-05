import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

fun getData(input: List<String>): List<List<List<Int>>> {
    return input.map { raw ->
        raw.split(" -> ").map { pair ->
            pair.split(",").map(String::toInt)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        getData(input).forEach { p ->
            if (p[0][1] == p[1][1]) {
                val y = p[0][1]
                for (i in min(p[0][0], p[1][0])..max(p[0][0], p[1][0])) {
                    val target = Pair(i, y)
                    map[target] = map.getOrDefault(target, 0) + 1
                }
            } else if (p[0][0] == p[1][0]) {
                val x = p[0][0]
                for (i in min(p[0][1], p[1][1])..max(p[0][1], p[1][1])) {
                    val target = Pair(x, i)
                    map[target] = map.getOrDefault(target, 0) + 1
                }
            }
        }
        return map.values.count { it >= 2 }
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        getData(input).forEach { p ->
            if (p[0][1] == p[1][1]) {
                val y = p[0][1]
                for (i in min(p[0][0], p[1][0])..max(p[0][0], p[1][0])) {
                    val target = Pair(i, y)
                    map[target] = map.getOrDefault(target, 0) + 1
                }
            } else if (p[0][0] == p[1][0]) {
                val x = p[0][0]
                for (i in min(p[0][1], p[1][1])..max(p[0][1], p[1][1])) {
                    val target = Pair(x, i)
                    map[target] = map.getOrDefault(target, 0) + 1
                }
            } else if (p[1][0] - p[0][0] == p[1][1] - p[0][1]) {
                val minX = min(p[0][0], p[1][0])
                val minY = min(p[0][1], p[1][1])

                for (i in 0..abs(p[1][0] - p[0][0])) {
                    val target = Pair(minX + i, minY + i)
                    map[target] = map.getOrDefault(target, 0) + 1
                }
            } else if (p[1][0] - p[0][0] == -p[1][1] + p[0][1]) {
                val minX = min(p[0][0], p[1][0])
                val bigY = max(p[0][1], p[1][1])

                for (i in 0..abs(p[1][0] - p[0][0])) {
                    val target = Pair(minX + i, bigY - i)
                    map[target] = map.getOrDefault(target, 0) + 1
                }
            }
        }
        return map.values.count { it >= 2 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
