fun main() {
    fun part1(input: List<String>): Int {
        return getMap(input)
            .fold(getCommand(input).first())
            .map { it.value.size }
            .sum()
    }

    fun part2(input: List<String>) {
        val map = getMap(input)
        getCommand(input).forEach { str ->
            map.fold(str)
        }
        printText(map)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    part2(testInput)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}

private fun getMap(input: List<String>): MutableMap<Int, MutableSet<Int>> {
    val map = mutableMapOf<Int, MutableSet<Int>>()
    input.filter {
        it.contains(",")
    }.forEach { line ->
        val p = line.split(",").map { it.toInt() }
        val list = map[p[0]] ?: mutableSetOf<Int>().also {
            map[p[0]] = it
        }
        list.add(p[1])
    }
    return map
}

private fun getCommand(input: List<String>): List<String> {
    return input.filter {
        it.contains("fold")
    }
}

private fun MutableMap<Int, MutableSet<Int>>.fold(command: String): MutableMap<Int, MutableSet<Int>> {
    val target = command.substring(command.indexOf("=") + 1).toInt()
    if (command.contains("y=")) {
        this.forEach { p ->
            p.value.remove(target)
            val set = mutableSetOf<Int>()
            p.value.forEach {
                if (it > target) {
                    val offset = it - target
                    set.add(target - offset)
                } else {
                    set.add(it)
                }
            }
            p.value.clear()
            p.value.addAll(set)
        }
    } else {
        val newMap = mutableMapOf<Int, MutableSet<Int>>()
        this.remove(target)
        this.forEach { p ->
            if (p.key > target) {
                val offset = p.key - target
                newMap[target - offset] = mutableSetOf<Int>().also {
                    it.addAll(p.value)
                    it.addAll(this[target - offset].orEmpty())
                }
            } else {
                newMap[p.key] = p.value.toMutableSet().also {
                    it.addAll(newMap[p.key].orEmpty())
                }
            }
        }
        this.clear()
        this.putAll(newMap)
    }
    return this
}

private fun printText(map: Map<Int, Set<Int>>) {
    val maxX = map.keys.maxOf { it }
    val maxY = map.values.maxOf { set ->
        set.maxOf { it }
    }

    for (j in 0..maxY) {
        for (i in 0..maxX) {
            print(if (map[i]?.contains(j) == true) "#" else " ")
        }
        println()
    }
}