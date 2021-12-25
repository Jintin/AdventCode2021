import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.*
import kotlin.math.min

private data class Block(
    val id: String,
    val belong: Char = '.',
    val room: Boolean = false
) {
    var canStay: Boolean = true
    var chess: Chess? = null
    val list = mutableSetOf<Block>()

    fun link(block: Block) {
        block.list.add(this)
        list.add(block)
    }

    companion object {
        fun buildListBlock(value: Char, size: Int) = buildList<Block> {
            repeat(size) {
                add(Block(value.toString() + it, value, true))
            }
            windowed(2) {
                it.first().link(it.last())
            }
        }
    }
}

private class Chess(
    val value: Char,
    var position: Block,
    var move: Int = 0
) {
    init {
        position.chess = this
    }

    val cost: Int = when (value) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> throw RuntimeException()
    }

    fun moveTo(newPosition: Block) {
        position.chess = null
        position = newPosition
        newPosition.chess = this
    }
}

private data class Game(
    private val listA: List<Block>,
    private val listB: List<Block>,
    private val listC: List<Block>,
    private val listD: List<Block>,
    private val chess: List<Chess>,
) {
    var minScore = Int.MAX_VALUE
    private var score = 0
    private val blocks = buildList<Block> {
        repeat(11) {
            add(Block((this.size + 1).toString()))
        }
        windowed(2) {
            it.first().link(it.last())
        }
        addAll(listA)
        addAll(listB)
        addAll(listC)
        addAll(listD)
    }

    init {
        blocks[2].link(listA[0])
        blocks[2].canStay = false
        blocks[4].link(listB[0])
        blocks[4].canStay = false
        blocks[6].link(listC[0])
        blocks[6].canStay = false
        blocks[8].link(listD[0])
        blocks[8].canStay = false
    }

    fun Char.perfect() = when (this) {
        'A' -> listA.perfect()
        'B' -> listB.perfect()
        'C' -> listC.perfect()
        'D' -> listD.perfect()
        else -> false
    }

    fun List<Block>.perfect(): Boolean {
        return none {
            val chess = it.chess
            chess != null && chess.value != it.belong
        }
    }

    fun Chess.canMove(block: Block): Boolean {
        if (!position.room && !block.room) {
            return false
        }
        if (block.room) {
            if (this.value != block.belong) {
                return false
            }
            if (!block.belong.perfect()) {
                return false
            }
        }
        return true
    }

    fun Chess.validPosition(): Map<Block, Int> {
        val result = mutableMapOf<Block, Int>()
        val q = LinkedList<Block>()
        val visit = mutableSetOf<Block>()
        if (!position.belong.perfect()) {
            q.offer(position)
        }

        var cost = 0
        while (q.isNotEmpty()) {
            var size = q.size
            while (size-- > 0) {
                val b = q.poll()
                if (visit.contains(b)) {
                    continue
                }
                visit.add(b)
                if (b.canStay && b.chess == null && this.canMove(b)) {
                    result[b] = cost
                }
                q.addAll(b.list.filter {
                    !visit.contains(it) && it.chess == null
                })
            }
            cost++
        }
        if (this.value.perfect() && position.belong != this.value) {
            val list = result.filter { it.key.belong == this.value }
            if (list.size == 1) {
                return list
            } else if (list.size >= 2) {
                val key = list.keys.maxByOrNull { it.id.last().digitToInt() }
                return list.filter { it.key == key }
            }
        }
        return result
    }

    fun iterate(deep: Int) {
        if (isOver()) {
            minScore = min(minScore, score)
            return
        }

        if (score > minScore || deep > chess.size * 2) {
            return
        }
        chess.forEach { c ->
            if (c.move < 2) {
                c.move++
                val existingPosition = c.position
                c.validPosition()
                    .forEach {
                        val (point, cost) = it
                        c.moveTo(point)
                        score += cost * c.cost
                        iterate(deep + 1)
                        score -= cost * c.cost
                        c.moveTo(existingPosition)
                    }
                c.move--
            }
        }
    }

    fun isOver(): Boolean {
        return chess.all {
            it.position.belong == it.value
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val listA = Block.buildListBlock('A', 2)
        val listB = Block.buildListBlock('B', 2)
        val listC = Block.buildListBlock('C', 2)
        val listD = Block.buildListBlock('D', 2)
        val game = Game(
            listA, listB, listC, listD, listOf(
                Chess(input[2][3], listA[0]),
                Chess(input[3][3], listA[1]),
                Chess(input[2][5], listB[0]),
                Chess(input[3][5], listB[1]),
                Chess(input[2][7], listC[0]),
                Chess(input[3][7], listC[1]),
                Chess(input[2][9], listD[0]),
                Chess(input[3][9], listD[1]),
            )
        )

        game.iterate(0)
        return game.minScore
    }

    fun part2(input: List<String>): Int {
        val listA = Block.buildListBlock('A', 4)
        val listB = Block.buildListBlock('B', 4)
        val listC = Block.buildListBlock('C', 4)
        val listD = Block.buildListBlock('D', 4)
        val game = Game(
            listA, listB, listC, listD, listOf(
                Chess(input[2][3], listA[0]),
                Chess('D', listA[1]),
                Chess('D', listA[2]),
                Chess(input[3][3], listA[3]),
                Chess(input[2][5], listB[0]),
                Chess('C', listB[1]),
                Chess('B', listB[2]),
                Chess(input[3][5], listB[3]),
                Chess(input[2][7], listC[0]),
                Chess('B', listC[1]),
                Chess('A', listC[2]),
                Chess(input[3][7], listC[3]),
                Chess(input[2][9], listD[0]),
                Chess('A', listD[1]),
                Chess('C', listD[2]),
                Chess(input[3][9], listD[3]),
            )
        )

        game.iterate(0)
        return game.minScore
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 12521)
    check(part2(testInput) == 44169)
    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
