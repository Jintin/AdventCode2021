import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Int {
        val data = getData(input)
        while (data.size != 1) {
            for (i in 1 until data.size) {
                val result = Point3D.join(data[0], data[i])
                if (result != null) {
                    data[0].addAll(result.second)
                    data.removeAt(i)
                    break
                }
            }
        }
        return data[0].size
    }

    fun part2(input: List<String>): Int {
        val data = getData(input)
        val anchors = mutableListOf(Point3D(0, 0, 0))
        while (data.size != 1) {
            for (i in 1 until data.size) {
                val result = Point3D.join(data[0], data[i])
                if (result != null) {
                    data[0].addAll(result.second)
                    data.removeAt(i)
                    anchors.add(result.first)
                    break
                }
            }
        }
        var max = 0
        for (i in anchors.indices) {
            for (j in i + 1 until anchors.size) {
                max = max(max, Point3D.distance(anchors[i], anchors[j]))
            }
        }
        return max
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}

private fun getData(input: List<String>): MutableList<MutableList<Point3D>> {
    val data = mutableListOf<MutableList<Point3D>>()
    var current = mutableListOf<Point3D>()
    input.forEach { str ->
        if (str.startsWith("--- ")) {
            current = mutableListOf()
        } else if (str == "") {
            data.add(current)
        } else {
            current.add(Point3D.from(str))
        }
    }
    data.add(current)
    return data
}

private data class Point3D(val x: Int, val y: Int, val z: Int) {

    companion object {
        fun from(str: String): Point3D {
            val list = str.split(",").map(String::toInt)
            return Point3D(list[0], list[1], list[2])
        }

        fun join(source: List<Point3D>, compare: List<Point3D>): Pair<Point3D, List<Point3D>>? {
            source.forEach { target ->
                for (type in 0 until 24) {
                    val list = compare.map { it.transform(type) }
                    compare.indices.forEach { index ->
                        val diff = target - list[index]
                        val count = list.map { it + diff }.count {
                            source.contains(it)
                        }
                        if (count >= 12) {
                            return Pair(diff, list.map { it + diff }.filter { !source.contains(it) })
                        }
                    }
                }
            }
            return null
        }

        fun distance(p1: Point3D, p2: Point3D) = abs(p1.x - p2.x) + abs(p1.y - p2.y) + abs(p1.z - p2.z)
    }

    fun sqrt() = sqrt((x * x + y * y + z * z).toDouble())

    operator fun minus(point: Point3D) = Point3D(x - point.x, y - point.y, z - point.z)

    operator fun plus(point: Point3D) = Point3D(x + point.x, y + point.y, z + point.z)

    fun transform(index: Int): Point3D {
        return when (index) {
            0 -> Point3D(x, y, z)
            1 -> Point3D(x, z, -y)
            2 -> Point3D(x, -y, -z)
            3 -> Point3D(x, -z, y)

            4 -> Point3D(y, -x, z)
            5 -> Point3D(y, z, x)
            6 -> Point3D(y, x, -z)
            7 -> Point3D(y, -z, -x)

            8 -> Point3D(-x, -y, z)
            9 -> Point3D(-x, -z, -y)
            10 -> Point3D(-x, y, -z)
            11 -> Point3D(-x, z, y)

            12 -> Point3D(-y, x, z)
            13 -> Point3D(-y, -z, x)
            14 -> Point3D(-y, -x, -z)
            15 -> Point3D(-y, z, -x)

            16 -> Point3D(z, y, -x)
            17 -> Point3D(z, x, y)
            18 -> Point3D(z, -y, x)
            19 -> Point3D(z, -x, -y)

            20 -> Point3D(-z, -y, -x)
            21 -> Point3D(-z, -x, y)
            22 -> Point3D(-z, y, x)
            23 -> Point3D(-z, x, -y)
            else -> throw RuntimeException("not support")
        }
    }
}
