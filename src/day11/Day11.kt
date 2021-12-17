package day11

import utils.readInput
import utils.times


const val FLASH_VALUE = 10

class Octopuses(input: List<String>) {
    val nrow = input.size
    val ncol = input[0].length
    val size = nrow * ncol
    var grid = allPoints().associateWith { (i, j) -> input[i][j].toString().toInt() }

    fun allPoints() = (0 until nrow) * (0 until ncol)

    fun neighbors(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        val points = mutableListOf<Pair<Int, Int>>()
        val (i, j) = point
        for ((di, dj) in (-1..1) * (-1..1)) {
            if (di == 0 && dj == 0) continue
            val nextI = i + di
            val nextJ = j + dj
            if (nextI in 0 until nrow && nextJ in 0 until ncol) points.add(nextI to nextJ)
        }
        return points
    }

    fun step(): Int {
        val flashed = mutableSetOf<Pair<Int, Int>>()
        grid = grid.mapValues { it.value + 1 }
        var toFlash = grid.filterValues { it >= FLASH_VALUE }.keys
        while (toFlash.isNotEmpty()) {
            val increases = toFlash.flatMap { neighbors(it) }.groupingBy { it }.eachCount()
            grid = grid.mapValues { increases.getOrDefault(it.key, 0) + it.value }
            flashed.addAll( toFlash )
            toFlash = grid.filterValues { it >= FLASH_VALUE }.keys - flashed
        }
        grid = grid.mapValues { if (it.key in flashed) 0 else it.value }
        return flashed.size
    }

    override fun toString() = buildString {
        for (i in 0 until nrow) {
            for (j in 0 until ncol) {
                append(grid[i to j])
            }
            append("\n")
        }
    }
}

fun part1(input: List<String>) = Octopuses(input).let{ octopuses ->
    (1..100).sumOf { _ ->
        octopuses.step()
    }
}

fun part2(input: List<String>) = Octopuses(input).let { octopuses ->
    generateSequence(1) { it + 1 }.first {
        val flashed = octopuses.step()
        flashed == octopuses.size
    }
}

fun main() {
    val subpackage = "day11"
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
