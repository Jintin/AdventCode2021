fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        input.map { it.toInt() }
            .reduce { a, b ->
            if (b > a) {
                count++
            }
            b
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0

        for (i in 3 until input.size) {
            if (input[i].toInt() > input[i - 3].toInt()) {
                count++
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
