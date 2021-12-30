package day20

import kotlin.system.measureTimeMillis
import utils.readInput
import utils.times


fun enhanceImage(input: List<String>, times: Int): Int {
    val algo = input.first().map { it == '#' }
    val imageChars = input.drop(2)
    val height = imageChars.size
    val width = imageChars[0].length
    val image = Array(height) { i -> Array(width) { j -> imageChars[i][j] == '#' } }
    val result = (1..times).fold(image) { prev, step ->
        val default = if (algo[0]) step % 2 == 0 else false
        val nextHeight = prev.size + 2
        val nextWidth = prev[0].size + 2
        Array(nextHeight) { i ->
            Array(nextWidth) { j ->
                val algoIndex = ((-1..1) * (-1..1)).map { (di, dj) ->
                    if (i + di in 1..(nextHeight - 2) && j + dj in 1..(nextWidth - 2)) prev[i + di - 1][j + dj - 1] else default
                }.reversed().withIndex().sumOf { (index, bit) -> if (bit) 1 shl index else 0 }
                algo[algoIndex]
            }
        }
    }
    return result.sumOf { row -> row.count { it } }
}

fun part1(input: List<String>) = enhanceImage(input, 2)

fun part2(input: List<String>) = enhanceImage(input, 50)

fun main() {
    val subpackage = "day20"
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
