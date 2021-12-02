fun main() {
    fun part1(input: List<String>): Long {
        var position = 0L
        var depth = 0L
        input.forEach {
            val list = it.split(" ")
            val value = list[1].toInt()
            when (list[0]) {
                "forward" -> position += value
                "up" -> depth -= value
                "down" -> depth += value
            }
        }
        return position * depth
    }

    fun part2(input: List<String>): Long {
        var position = 0L
        var aim = 0L
        var depth = 0L
        input.forEach {
            val list = it.split(" ")
            val value = list[1].toInt()
            when (list[0]) {
                "forward" -> {
                    position += value
                    depth += aim * value
                }
                "up" -> aim -= value
                "down" -> aim += value
            }
        }
        return position * depth
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
