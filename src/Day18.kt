import java.lang.RuntimeException

fun main() {
    fun part1(input: List<String>): Int {
        return input.map(SnailFish::from)
            .reduce(SnailFish::merge)
            .sum()
    }

    fun part2(input: List<String>): Int {
        var max = 0
        val list = input.map(SnailFish::from)
        list.indices.forEach { i ->
            list.indices.forEach { j ->
                if (i != j) {
                    val value = SnailFish.merge(list[i].copy(), list[j].copy()).sum()
                    if (value > max) {
                        max = value
                    }
                }
            }
        }
        return max
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)
    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

private sealed class SnailFish {
    private data class Number(var value: Int) : SnailFish() {

        override fun sum(): Int = value

        override fun add(a: Int, b: Int) {
            value += a + b
        }

        override fun explode(level: Int): Pair<Boolean, Pair<Int, Int>> = Pair(false, Pair(0, 0))

        override fun split(): Boolean = false

        override fun copy() = Number(value)
    }

    private data class Node(var left: SnailFish, var right: SnailFish) : SnailFish() {

        override fun sum(): Int = left.sum() * 3 + right.sum() * 2

        override fun add(a: Int, b: Int) {
            left.add(a, 0)
            right.add(0, b)
        }

        override fun explode(level: Int): Pair<Boolean, Pair<Int, Int>> {
            if (level == 4) {
                (left as? Node)?.let {
                    left = Number(0)
                    val leftValue = (it.left as Number).value
                    val rightValue = (it.right as Number).value
                    right.add(rightValue, 0)
                    return Pair(true, Pair(leftValue, 0))
                } ?: (right as? Node)?.let {
                    right = Number(0)
                    val leftValue = (it.left as Number).value
                    val rightValue = (it.right as Number).value
                    left.add(0, leftValue)
                    return Pair(true, Pair(0, rightValue))
                }
            }
            val resultLeft = left.explode(level + 1)
            if (resultLeft.first) {
                if (resultLeft.second.second != 0) {
                    right.add(resultLeft.second.second, 0)
                    return Pair(true, Pair(resultLeft.second.first, 0))
                }
                return resultLeft
            }
            val resultRight = right.explode(level + 1)
            if (resultRight.first) {
                if (resultRight.second.first != 0) {
                    left.add(0, resultRight.second.first)
                    return Pair(true, Pair(0, resultRight.second.second))
                }
                return resultRight
            }
            return Pair(false, Pair(0, 0))
        }

        override fun split(): Boolean = splitObj(left) || splitObj(right)

        private fun splitObj(obj: SnailFish): Boolean {
            return when (obj) {
                is Node -> obj.split()
                is Number -> {
                    obj.takeIf { it.value >= 10 }?.let { number ->
                        val result = Node(Number(number.value / 2), Number(number.value / 2 + number.value % 2))
                        if (left == number) {
                            left = result
                        } else {
                            right = result
                        }
                        true
                    } ?: false
                }
            }
        }

        override fun copy() = Node(left.copy(), right.copy())
    }

    companion object {

        fun from(str: String) = parse(str, 0, str.length)

        private fun parse(str: String, start: Int, end: Int): SnailFish {
            if (str[start].isDigit()) {
                return Number((str[start] - '0'))
            } else {
                var count = 0
                var index = start + 1
                while (index < end) {
                    when (str[index]) {
                        ',' -> {
                            if (count == 0) {
                                return Node(parse(str, start + 1, index), parse(str, index + 1, end))
                            }
                        }
                        '[' -> count++
                        ']' -> count--
                    }
                    index++
                }
                throw RuntimeException()
            }
        }

        fun merge(a: SnailFish, b: SnailFish): SnailFish = Node(a, b).also {
            while (true) {
                var change = false
                if (it.explode(1).first) {
                    change = true
                }
                if (!change && it.split()) {
                    change = true
                }
                if (!change) {
                    break
                }
            }
        }
    }

    abstract fun sum(): Int
    abstract fun add(a: Int, b: Int)
    abstract fun explode(level: Int): Pair<Boolean, Pair<Int, Int>>
    abstract fun split(): Boolean
    abstract fun copy(): SnailFish
}
