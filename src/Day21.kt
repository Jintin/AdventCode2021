import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val a = Player(input[0].last().digitToInt())
        val b = Player(input[1].last().digitToInt())
        val times = rule1(a, b, 1, 1000)
        return min(a.score, b.score) * (times - 1)
    }

    fun part2(input: List<String>): Long {
        val a = Player(input[0].last().digitToInt())
        val b = Player(input[1].last().digitToInt())
        val map = mutableMapOf<Int, Long>()
        for (i in 1..3) {
            for (j in 1..3) {
                for (k in 1..3) {
                    val value = i + j + k
                    map[value] = map.getOrDefault(value, 0) + 1
                }
            }
        }
        rule2(a, b, 21, map)
        return max(a.win, b.win)
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}

private fun rule1(a: Player, b: Player, dice: Int, target: Int): Int {
    if (b.score >= target) {
        return dice
    }
    val value = dice * 3 + 3
    a.move(value)
    return rule1(b, a, dice + 3, target)
}

private fun rule2(a: Player, b: Player, target: Int, map: Map<Int, Long>, count: Long = 1) {
    if (b.score >= target) {
        b.win += count
        return
    }
    map.forEach {
        a.move(it.key)
        rule2(b, a, target, map, it.value * count)
        a.revert(it.key)
    }
}

private data class Player(var index: Int, var score: Int = 0, var win: Long = 0) {
    fun move(step: Int) {
        index += step
        index--
        index %= 10
        index++
        score += index
    }

    fun revert(step: Int) {
        score -= index
        index -= step
        while (index <= 0) {
            index += 10
        }
    }
}
