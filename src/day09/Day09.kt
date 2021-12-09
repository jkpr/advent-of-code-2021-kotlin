package day09

import readInput


class DepthMap(val input: List<String>) {
    val coords = input.indices.product(input[0].indices)
    val depths = coords.associateWith { (i, j) -> input[i][j] }.withDefault { '@' }
    val lowPoints = coords.filter { point ->
        depths.getValue(point) < point.neighbors().minOf { depths.getValue(it) }
    }
    val basins: Collection<List<Pair<Int, Int>>>

    init {
        val coordsWithBasinLabels = mutableMapOf<Pair<Int, Int>, Int>()
        var label = 0
        coords.forEach { point -> searchBasin(point, coordsWithBasinLabels, label++) }
        basins = coordsWithBasinLabels.entries.groupBy({ it.value }) { it.key }.values
    }

    private fun searchBasin(point: Pair<Int, Int>, coordsWithBasinLabels: MutableMap<Pair<Int,Int>, Int>, label: Int) {
        if (point !in coordsWithBasinLabels && depths.getValue(point) < '9') {
            coordsWithBasinLabels[point] = label
            point.neighbors().forEach { searchBasin(it, coordsWithBasinLabels, label) }
        }
    }
}

fun IntRange.product(other: IntRange) = this.flatMap { i -> other.map { j -> i to j } }

fun Pair<Int, Int>.neighbors() = listOf(
    this.first - 1 to this.second,
    this.first + 1 to this.second,
    this.first     to this.second - 1,
    this.first     to this.second + 1,
)

fun part1(input: List<String>) = DepthMap(input).run {
    lowPoints.sumOf { depths[it].toString().toInt() + 1 }
}

fun part2(input: List<String>) = DepthMap(input).run {
    basins.map { it.size }.sortedBy { it }.takeLast(3).reduce { a, b -> a * b }
}

fun main() {
    val subpackage = "day09"
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
