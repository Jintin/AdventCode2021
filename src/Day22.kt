import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val scope = Region(listOf(-50, 50), listOf(-50, 50), listOf(-50, 50), false)
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

private data class Region(
    val x: List<Int>,
    val y: List<Int>,
    val z: List<Int>,
    var light: Boolean
) {
    fun count(): Long = (if (light) 1 else -1L) *
            (x[1] - x[0] + 1) *
            (y[1] - y[0] + 1) *
            (z[1] - z[0] + 1)

    fun clip(region: Region): Region? {
        val newX = listOf(max(region.x[0], x[0]), min(region.x[1], x[1]))
        val newY = listOf(max(region.y[0], y[0]), min(region.y[1], y[1]))
        val newZ = listOf(max(region.z[0], z[0]), min(region.z[1], z[1]))
        if (newX[1] < newX[0] || newY[1] < newY[0] || newZ[1] < newZ[0]) {
            return null
        }

        return Region(newX, newY, newZ, light)
    }
}

private fun MutableList<Region>.join(region: Region) {
    val extra = mutableListOf<Region>()
    this.mapNotNull { it.clip(region) }.forEach { clip ->
        extra.add(
            Region(
                clip.x, clip.y, clip.z, if (region.light && clip.light) {
                    false
                } else if (!region.light && !clip.light) {
                    true
                } else {
                    region.light
                }
            )
        )
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
            it.drop(2).split("..").map(String::toInt)
        }
        Region(value[0], value[1], value[2], on)
    }
}