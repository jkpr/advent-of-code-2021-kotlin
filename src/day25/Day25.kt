package day25

import utils.readInput


typealias Point = Pair<Int, Int>
val Point.i get() = first
val Point.j get() = second
operator fun Point.plus(other: Point) = (first + other.i) to (second + other.j)
operator fun Point.rem(other: Point) = (first % other.i) to (second % other.j)

val MOVE_EAST = 0 to 1
val MOVE_SOUTH = 1 to 0

data class SeaCucumbers(val east: Set<Point>, val south: Set<Point>, val dim: Point) {
    fun march(): SeaCucumbers {
        val eastToMove = east.associateWith { (it + MOVE_EAST) % dim }.filterValues { it !in east && it !in south }
        val newEast = (east - eastToMove.keys) + eastToMove.values
        val southToMove = south.associateWith { (it + MOVE_SOUTH) % dim }.filterValues { it !in newEast && it !in south }
        val newSouth = (south - southToMove.keys) + southToMove.values
        return SeaCucumbers(newEast, newSouth, dim)
    }

    override fun toString() = buildString {
        for (i in 0 until dim.i) {
            for (j in 0 until dim.j) {
                val ch = when (i to j) {
                    in east -> '>'
                    in south -> 'v'
                    else -> '.'
                }
                append(ch)
            }
            append('\n')
        }
    }
}

fun makeSeaCucumbers(input: List<String>): SeaCucumbers {
    val seaFloor = input.flatMapIndexed { i, line -> line.mapIndexed{ j, ch -> (i to j) to ch }}
    val east = seaFloor.filter { it.second == '>' }.map { it.first }.toSet()
    val south = seaFloor.filter { it.second == 'v' }.map { it.first }.toSet()
    val dim = input.size to input[0].length
    return SeaCucumbers(east, south, dim)
}

fun part1(input: List<String>) = generateSequence( makeSeaCucumbers(input) ) {
    it.march()
}.withIndex().zipWithNext().first { (prev, next) ->
    prev.value == next.value
}.second.index


fun main() {
    val subpackage = "day25"
    val input = readInput(subpackage, "input")
//    val input = readInput(subpackage, "test_input")
    val start = System.currentTimeMillis()
    val result1 = part1(input)
    println("Part 1: $result1")
    val end = System.currentTimeMillis()
    val delta = (end - start) / 1000.0
    println("Time elapsed: $delta seconds")
}
