import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val p = getPair(input)
        var max = 0
        for (i in -1000..1000) {
            for (j in -1000..1000) {
                val result = checkResult(i, j, p)
                if (result is Result.Success) {
                    max = max(max, result.height)
                }
            }
        }
        return max
    }

    fun part2(input: List<String>): Int {
        val p = getPair(input)
        var count = 0
        for (i in -1000..1000) {
            for (j in -1000..1000) {
                val result = checkResult(i, j, p)
                if (result is Result.Success) {
                    count++
                }
            }
        }
        return count
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

private sealed class Result {
    data class Success(val height: Int) : Result()
    object Fail : Result()
}

private fun checkResult(initSpeedX: Int, initSpeedY: Int, ranges: Pair<IntRange, IntRange>): Result {
    var x = 0
    var y = 0
    var maxY = 0
    var speedX = initSpeedX
    var speedY = initSpeedY
    val absMaxX = max(abs(ranges.first.first), abs(ranges.first.last))
    val absMinY = min(ranges.second.first, ranges.second.last)
    while (x !in ranges.first || y !in ranges.second) {
        x += speedX
        if (abs(x) > absMaxX) {
            return Result.Fail
        } else if (speedX > 0) {
            speedX--
        } else if (speedX < 0) {
            speedX++
        } else if (x !in ranges.first) {
            return Result.Fail
        }
        y += speedY
        maxY = max(maxY, y)
        speedY--
        if (y < absMinY) {
            return Result.Fail
        }
    }
    return Result.Success(maxY)
}

private fun getPair(input: List<String>): Pair<IntRange, IntRange> {
    return input[0].run {
        substring(indexOf("x=") + 2)
    }
        .split(", y=")
        .map {
            val list = it.split("..").map(String::toInt)
            IntRange(list[0], list[1])
        }.run {
            Pair(this[0], this[1])
        }
}
