package day21

import kotlin.system.measureTimeMillis
import utils.readInput
import kotlin.math.min


data class Player(val square: Int, val score: Int = 0) {
    fun moveForward(count: Int): Player {
        if (count == 0) return this
        val newSquare = (square - 1 + count) % 10 + 1
        return Player(newSquare, score + newSquare)
    }
}

data class DeterministicState(val p1: Player, val p2: Player, val p1Turn: Boolean = true, val die: Int = 0, val rollCount: Int = 0) {
    fun gameOver() = p1.score >= 1000 || p2.score >= 1000

    fun minScore() = min(p1.score, p2.score)

    fun next(): DeterministicState {
        val rolls = listOf(
            die % 100 + 1,
            (die + 1) % 100 + 1,
            (die + 2) % 100 + 1
        )
        val moveForward = rolls.sumOf { it }
        val nextP1 = if (p1Turn) p1.moveForward(moveForward) else p1
        val nextP2 = if (p1Turn) p2 else p2.moveForward(moveForward)
        return DeterministicState(nextP1, nextP2, !p1Turn, rolls.last(), rollCount + 3)
    }
}

fun setup(input: List<String>): DeterministicState {
    val (p1, p2) = input.map { it.split(" ").last().toInt() }.map { Player(it) }
    return DeterministicState(p1, p2)
}

typealias DiracCache = MutableMap<DiracState, Pair<Long, Long>>
fun emptyCache() = mutableMapOf<DiracState, Pair<Long, Long>>()

data class DiracState(val p1: Player, val p2: Player, val p1Turn: Boolean = true) {
    val tally get() = if (p1.score >= 21) 1L to 0L else if (p2.score >= 21) 0L to 1L else 0L to 0L

    fun gameOver() = p1.score >= 21 || p2.score >= 21

    fun allNextStates() = buildList(3 * 3 * 3) {
        for (i in 1..3) for (j in 1..3) for (k in 1..3) {
            val moveForward = i + j + k
            val nextP1 = if (p1Turn) p1.moveForward(moveForward) else p1
            val nextP2 = if (p1Turn) p2 else p2.moveForward(moveForward)
            add(DiracState(nextP1, nextP2, !p1Turn))
        }
    }

    fun getWinTally(cache: DiracCache = emptyCache()): Pair<Long, Long> = cache[this] ?: if (gameOver()) tally else
        allNextStates().map { it.getWinTally(cache) }.fold(0L to 0L) { a, b -> a + b }.also { cache[this] = it }
}

fun setupDirac(input: List<String>): DiracState {
    val (p1, p2) = input.map { it.split(" ").last().toInt() }.map { Player(it) }
    return DiracState(p1, p2)
}

operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = (first + other.first) to (second + other.second)
fun Pair<Long, Long>.max() = if (first > second) first else second

fun part1(input: List<String>) = generateSequence(setup(input)) { it.next() }.first { it.gameOver() }.let { it.minScore() * it.rollCount }

fun part2(input: List<String>) = setupDirac(input).getWinTally().max()

fun main() {
    val subpackage = "day21"
    val input = readInput(subpackage, "input")
//    val input = readInput(subpackage, "test_input")
    val elapsed = measureTimeMillis {
        val result1 = part1(input)
        val result2 = part2(input)
        println("Part 1: $result1")
        println("Part 2: $result2")
    }
    println("Time elapsed: ${elapsed / 1000.0} seconds")
}
