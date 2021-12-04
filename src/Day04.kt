import java.lang.Integer.max
import java.lang.Integer.min

data class Record(val x: Int, val y: Int, val value: Int, val mark: Boolean = false)

class Board {
    private var win = false
    private val x = mutableListOf(0, 0, 0, 0, 0)
    private val y = mutableListOf(0, 0, 0, 0, 0)
    private val map = mutableMapOf<Int, Record>()

    fun set(x: Int, y: Int, value: Int) {
        map[value] = Record(x, y, value)
    }

    fun label(value: Int): Boolean {
        if (win) {
            return false
        }
        map[value]?.let { p ->
            map[value] = p.copy(mark = true)
            x[p.x]++
            y[p.y]++
            if (y[p.y] == 5 ||x[p.x] == 5) {
                win = true
                return true
            }
        }
        return false
    }

    fun count(): Int {
        return map.values.filter { !it.mark }.sumOf { it.value }
    }

    companion object {
        fun getAllBoard(input: List<String>): List<Board> {
            val list = mutableListOf<Board>()
            for (i in 0..(input.size - 2) / 6) {
                val board = Board()

                for (j in 0 until 5) {
                    input[i * 6 + j + 2]
                        .trim()
                        .split("\\s+".toRegex())
                        .forEachIndexed { index, s ->
                            board.set(j, index, s.toInt())
                        }
                }
                list.add(board)
            }
            return list
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val list = Board.getAllBoard(input)
        val order = input[0].split(",").map { it.toInt() }
        var max = -1
        order.forEach { value ->
            list.forEach {
                if (it.label(value)) {
                    max = max(max, it.count() * value)
                }
            }
            if (max != -1) {
                return max
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val list = Board.getAllBoard(input)
        val order = input[0].split(",").map { it.toInt() }
        var count = 0
        order.forEach { value ->
            var min = Integer.MAX_VALUE
            list.forEach {
                if (it.label(value)) {
                    count++
                    min = min(min, it.count() * value)
                }
            }
            if (count == list.size) {
                return min
            }
        }

        return -1
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
