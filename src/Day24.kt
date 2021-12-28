fun main() {
    fun part1(input: List<String>): Long {
        return iterate(input, true)
    }

    fun part2(input: List<String>): Long {
        return iterate(input, false)
    }

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}

private data class Value(var w: Long, var x: Long = 0, var y: Long = 0, var z: Long = 0) {
    fun getValue(key: String) = when (key) {
        "w" -> w
        "x" -> x
        "y" -> y
        "z" -> z
        else -> key.toLong()
    }
}

private class Command(private val input: List<String>) {

    private var xSet = false
    private var ySet = false

    fun build(): (Long, Long) -> Long {
        var formula: ((Value) -> Value)? = null
        input.forEach {
            formula = execute(it.split(" "), formula)
        }
        return { inpW: Long, inpZ: Long ->
            formula!!.invoke(Value(inpW, 0, 0, inpZ)).z
        }
    }

    private fun getCommand(string: String): (Long, Long) -> Long {
        return when (string) {
            "add" -> { a, b -> a + b }
            "mul" -> { a, b -> a * b }
            "div" -> { a, b -> a / b }
            "mod" -> { a, b -> a % b }
            "eql" -> { a, b -> if (a == b) 1 else 0 }
            else -> throw RuntimeException(string)
        }
    }

    private fun execute(data: List<String>, previousFormula: ((Value) -> Value)?): ((Value) -> Value)? {
        val command = data[0]
        val key = data[1]
        val key2 = data[2]
        if (command == "div" && key2 == "1") {
            return previousFormula
        }
        if (command == "mul" && key2 == "0") {
            if (key == "x" && !xSet) {
                xSet = true
                return previousFormula
            }
            if (key == "y" && !ySet) {
                ySet = true
                return previousFormula
            }
        }
        return { value: Value ->
            val newValue = previousFormula?.invoke(value) ?: value
            val result = getCommand(command).invoke(newValue.getValue(key), newValue.getValue(key2))
            when (key) {
                "w" -> newValue.w = result
                "x" -> newValue.x = result
                "y" -> newValue.y = result
                "z" -> newValue.z = result
            }
            value
        }
    }
}

private fun iterate(input: List<String>, reverse: Boolean): Long {
    val commands = mutableListOf<(Long, Long) -> Long>()
    var index = 0

    while (index * 18 < input.size) {
        val list = input.subList(index * 18 + 1, (index + 1) * 18)
        val formula = Command(list).build()
        commands.add(formula)
        index++
    }
    return iterate(commands, 0, 0, 0, mutableMapOf(), reverse) ?: -1
}

private fun iterate(
    commands: List<(Long, Long) -> Long>,
    index: Int,
    prev: Long,
    z: Long,
    cache: MutableMap<Int, MutableSet<Long>>,
    reverse: Boolean = false
): Long? {
    val set = cache.getOrPut(index) { mutableSetOf() }
    if (set.contains(z)) {
        return null
    }
    val range = if (reverse) 9L downTo 1 else 1L..9
    for (w in range) {
        val current = prev * 10 + w
        val result = commands[index].invoke(w, z)
        if (index < commands.size - 1) {
            iterate(commands, index + 1, current, result, cache, reverse)?.let {
                return it
            }
        } else {
            if (result == 0L) {
                return current
            }
        }
    }
    set.add(z)
    return null
}