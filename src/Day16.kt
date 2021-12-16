import java.lang.RuntimeException

fun main() {
    fun part1(input: List<String>): Int {
        val p = Parser(getString(input)).parse()
        return p.sumVersion()
    }

    fun part2(input: List<String>): Long {
        val p = Parser(getString(input)).parse()
        return p.calculate()
    }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun getString(input: List<String>): String =
    input[0].map(map::get) .joinToString(separator = "")

private data class Packet(
    val version: Int, // first 3 bit
    val type: Int, // later 3 bit
    var value: Long = 0, // if type = 4
    val subPacket: MutableList<Packet> = mutableListOf(), // if type != 4
) {
    fun sumVersion(): Int = version + subPacket.sumOf(Packet::sumVersion)

    fun calculate(): Long = when (type) {
        0 -> subPacket.sumOf(Packet::calculate)
        1 -> subPacket.map(Packet::calculate).reduce(Long::times)
        2 -> subPacket.minOf(Packet::calculate)
        3 -> subPacket.maxOf(Packet::calculate)
        4 -> value
        5 -> compareValue { l1, l2 -> l1 > l2 }
        6 -> compareValue { l1, l2 -> l1 < l2 }
        7 -> compareValue(Long::equals)
        else -> throw RuntimeException("not support type")
    }

    private fun compareValue(op: (Long, Long) -> Boolean): Long =
        if (op(subPacket[0].calculate(), subPacket[1].calculate())) 1 else 0
}

private class Parser(private val string: String) {
    private var index: Int = 0

    fun parse(): Packet = Packet(parseValue(3), parseValue(3)).also {
        if (it.type == 4) {
            var ending = false
            var value = 0L
            while (!ending) {
                ending = parseValue(1) == 0
                value = value * 16 + parseValue(4)
            }
            it.value = value
        } else {
            val length = parseValue(1)
            it.subPacket.addAll(
                if (length == 0) {
                    val size = parseValue(15)
                    parseWithLength(index + size)
                } else {
                    val count = parseValue(11)
                    parseWithCount(count)
                }
            )
        }
    }

    private fun parseValue(length: Int): Int {
        var value = 0
        repeat(length) {
            value = value * 2 + (string[index++] - '0')
        }
        return value
    }

    private fun parseWithCount(count: Int): List<Packet> {
        val list = mutableListOf<Packet>()
        repeat(count) {
            val p = parse()
            list.add(p)
        }
        return list
    }

    private fun parseWithLength(end: Int): List<Packet> {
        val list = mutableListOf<Packet>()
        while (index < end) {
            val p = parse()
            list.add(p)
        }
        return list
    }
}

private val map = mutableMapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)