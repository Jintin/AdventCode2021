fun main() {
    fun part1(input: List<String>): Int {
        val map = getMap(input)
        return map["start"]!!.countPaths()
    }

    fun part2(input: List<String>): Int {
        val map = getMap(input)
        return map["start"]!!.countPaths(true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 19)
    check(part2(testInput) == 103)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun Node.countPaths(extraVisit: Boolean = false): Int {
    return if (name == "end") {
        1
    } else {
        var count = 0
        iterate { node ->
            val canVisit = node.canVisit()
            if (canVisit || extraVisit) {
                node.visit()
                count += node.countPaths(extraVisit && canVisit)
                node.leave()
            }
        }
        count
    }
}

private fun getMap(input: List<String>): MutableMap<String, Node> {
    val map = mutableMapOf<String, Node>()
    input.forEach { connection ->
        val pair = connection.split("-")
        val first = getNode(pair[0], map)
        val second = getNode(pair[1], map)
        if (first.name == "start") {
            first.link(second)
        } else if (second.name == "start") {
            second.link(first)
        } else {
            first.link(second)
            second.link(first)
        }
    }
    return map
}

private fun getNode(name: String, map: MutableMap<String, Node>): Node {
    return map[name] ?: Node(name).also {
        map[name] = it
    }
}

private class Node(val name: String) {
    private var count = 0
    private val links: MutableSet<Node> = mutableSetOf()

    fun link(node: Node) {
        links.add(node)
    }

    fun canVisit(): Boolean = name.lowercase() != name || count == 0

    fun iterate(action: (Node) -> Unit) {
        links.forEach(action)
    }

    fun visit() {
        count++
    }

    fun leave() {
        count--
    }
}