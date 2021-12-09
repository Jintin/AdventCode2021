import java.lang.RuntimeException

fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        input.forEach { raw ->
            count += raw.split(" | ")[1]
                .split(" ")
                .count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        input.forEach { data ->
            val map = mutableMapOf<String, Int>()
            val raw = data.split(" | ")
            val list = raw[0].split(" ").sortedBy { it.length }
            val n1 = list[0]
            val n7 = list[1]
            val n4 = list[2]
            val n8 = list[9]
            val n3 = findContainTarget(listOf(list[3], list[4], list[5]), list[0])[0]
            val n9 = findContainTarget(listOf(list[6], list[7], list[8]), n3)[0]
            val e = findDiff(list[9], n9)[0]
            val n2 = listOf(list[3], list[4], list[5]).filter {
                it.contains(e.toString())
            }[0]
            val n5 = listOf(list[3], list[4], list[5]).filter { it != n2 && it != n3 }[0]
            val n6 = findContainTarget(listOf(list[6], list[7], list[8]), n5).filter { it != n9 }[0]
            val n0 = listOf(list[6], list[7], list[8]).filter { it != n6 && it != n9 }[0]
            map[n0.toCharArray().also { it.sort() }.concatToString()] = 0
            map[n1.toCharArray().also { it.sort() }.concatToString()] = 1
            map[n2.toCharArray().also { it.sort() }.concatToString()] = 2
            map[n3.toCharArray().also { it.sort() }.concatToString()] = 3
            map[n4.toCharArray().also { it.sort() }.concatToString()] = 4
            map[n5.toCharArray().also { it.sort() }.concatToString()] = 5
            map[n6.toCharArray().also { it.sort() }.concatToString()] = 6
            map[n7.toCharArray().also { it.sort() }.concatToString()] = 7
            map[n8.toCharArray().also { it.sort() }.concatToString()] = 8
            map[n9.toCharArray().also { it.sort() }.concatToString()] = 9
            val value = cal(raw[1].split(" "), map)
            count += value
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 7)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun cal(list: List<String>, map: Map<String, Int>): Int {
    var value = 0
    list.forEach {
        value *= 10
        value += map[it.toCharArray().also { it.sort() }.concatToString()] ?: throw RuntimeException(it)
    }
    return value
}

private fun findDiff(text1: String, text2: String): List<Char> {
    val list = mutableListOf<Char>()
    for (a in text1) {
        if (a !in text2) {
            list.add(a)
        }
    }
    return list
}

private fun findContainTarget(list: List<String>, target: String): List<String> {
    val result = mutableListOf<String>()
    for (str in list) {
        var success = true
        for (a in target) {
            if (a !in str) {
                success = false
            }
        }
        if (success) {
            result.add(str)
        }
    }
    return result
}