import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        input.filter(::valid)
            .forEach { data ->
                val stack = Stack<Char>()
                data.forEach {
                    if (it in startChars) {
                        stack.push(it)
                    } else {
                        val c = stack.pop()
                        if (charMap[it] != c) {
                            count += scoreMap1[it] ?: 0
                        }
                    }
                }
            }
        return count
    }

    fun part2(input: List<String>): Long {
        val list = mutableListOf<Long>()
        input.filter(::notComplete)
            .forEach { data ->
                val stack = Stack<Char>()
                data.forEach {
                    if (it in startChars) {
                        stack.push(it)
                    } else {
                        stack.pop()
                    }
                }
                var count = 0L
                while (stack.isNotEmpty()) {
                    val c = stack.pop()
                    count *= 5
                    count += scoreMap2[c] ?: 0
                }
                list.add(count)
            }
        list.sort()
        return list[list.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)
    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private var startChars = setOf('(', '[', '{', '<')
private val charMap = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
private val scoreMap1 = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val scoreMap2 = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

private fun valid(data: String): Boolean {
    var count = 0
    data.forEach {
        if (it in startChars) {
            count++
        } else {
            count--
            if (count < 0) {
                return false
            }
        }
    }
    return true
}

private fun notComplete(data: String): Boolean {
    val stack = Stack<Char>()
    data.forEach {
        if (it in startChars) {
            stack.push(it)
        } else {
            if (stack.isEmpty()) {
                return false
            }
            val c = stack.pop()
            if (charMap[it] != c) {
                return false
            }
        }
    }
    return stack.isNotEmpty()
}