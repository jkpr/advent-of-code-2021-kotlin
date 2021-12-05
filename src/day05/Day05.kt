package day05

import readInput
import java.lang.Integer.max


data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    fun pointsToList(): List<Pair<Int, Int>> {
        val dx = if (x1 == x2) 0 else if (x1 < x2) 1 else -1
        val dy = if (y1 == y2) 0 else if (y1 < y2) 1 else -1
        val nSteps = max(dx * (x2 - x1), dy * (y2 - y1))
        return (0..nSteps).map { (x1 + dx * it) to (y1 + dy * it) }
    }

    fun isDiagonal() = x1 != x2 && y1 != y2
}

fun countVents(input: List<String>, diagonals: Boolean): Int {
    return input.flatMap {
        val (x1, y1, x2, y2) = """\d+""".toRegex().findAll(it).map { found -> found.value.toInt() }.toList()
        val line = Line(x1, y1, x2, y2)
        if (diagonals || !line.isDiagonal()) line.pointsToList() else listOf()
    }.groupingBy { it }.eachCount().count { it.value > 1 }
}

fun part1(input: List<String>): Int {
    return countVents(input, diagonals = false)
}

fun part2(input: List<String>): Int {
    return countVents(input, diagonals = true)
}

fun main() {
    val subpackage = "day05"
    val input = readInput(subpackage, "input")
//    val input = readInput(subpackage, "test_input")
    val start = System.currentTimeMillis()
    val result1 = part1(input)
    val result2 = part2(input)
    println("Part 1: $result1")
    println("Part 2: $result2")
    val end = System.currentTimeMillis()
    val delta = (end - start) / 1000.0
    println("Time elapsed: $delta seconds")
}
