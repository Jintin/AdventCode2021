import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val scope = Region(IntRange(-50, 50), IntRange(-50, 50), IntRange(-50, 50), false)
        val regions = getRegions(input).mapNotNull { it.clip(scope) }
        val list = mutableListOf<Region>()
        regions.forEach(list::join)
        return list.sumOf { it.count() }
    }

    fun part2(input: List<String>): Long {
        val regions = getRegions(input)
        val list = mutableListOf<Region>()
        regions.forEach(list::join)
        return list.sumOf { it.count() }
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 474140L)
    check(part2(testInput) == 2758514936282235)
    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}

private fun IntRange.length(): Int = last - first + 1

private data class Region(
    val x: IntRange,
    val y: IntRange,
    val z: IntRange,
    var light: Boolean
) {
    fun count(): Long = (if (light) 1 else -1L) * x.length() * y.length() * z.length()

    fun clip(region: Region): Region? {
        val newX = IntRange(max(region.x.first, x.first), min(region.x.last, x.last))
        val newY = IntRange(max(region.y.first, y.first), min(region.y.last, y.last))
        val newZ = IntRange(max(region.z.first, z.first), min(region.z.last, z.last))
        if (newX.last < newX.first || newY.last < newY.first || newZ.last < newZ.first) {
            return null
        }

        return Region(newX, newY, newZ, light)
    }
}

private fun MutableList<Region>.join(region: Region) {
    val extra = mutableListOf<Region>()
    this.mapNotNull { it.clip(region) }
        .forEach { clip ->
            Region(
                clip.x, clip.y, clip.z, if (region.light && clip.light) {
                    false
                } else if (!region.light && !clip.light) {
                    true
                } else {
                    region.light
                }
            ).apply(extra::add)
        }
    if (region.light) {
        add(region)
    }
    addAll(extra)
}

private fun getRegions(input: List<String>): List<Region> {
    return input.map { str ->
        val on = str.startsWith("on")
        val value = str.drop(if (on) 3 else 4).split(",").map {
            val list = it.drop(2).split("..").map(String::toInt)
            IntRange(list[0], list[1])
        }
        Region(value[0], value[1], value[2], on)
    }
}