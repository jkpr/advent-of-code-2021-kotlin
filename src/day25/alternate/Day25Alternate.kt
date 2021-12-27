package day25.alternate

import utils.readInput
import kotlin.system.measureTimeMillis


fun part1(input: List<String>): Int {
    val yDim = input.size
    val xDim = input[0].length
    val seaFloor = Array(yDim) { y -> Array(xDim) { x -> (input[y][x]) } }
    val nextSeaFloor = Array(yDim) { y -> Array(xDim) { x -> seaFloor[y][x] } }

    fun march(herd: Char, dx: Int, dy: Int): Boolean {
        var movement = false
        for (y in 0 until yDim) for (x in 0 until xDim) {
            if (seaFloor[y][x] != herd) continue
            val xx = (x + dx) % xDim
            val yy = (y + dy) % yDim
            if (seaFloor[yy][xx] != '.') continue
            movement = true
            nextSeaFloor[yy][xx] = herd
            nextSeaFloor[y][x] = '.'
        }
        for (y in 0 until yDim) for (x in 0 until xDim) seaFloor[y][x] = nextSeaFloor[y][x]
        return movement
    }

    var n = 0
    do {
        n++
        val movedEast = march('>', 1, 0)
        val movedSouth = march('v', 0, 1)
    } while (movedEast || movedSouth)
    return n
}



fun main() {
    val subpackage = "day25"
    val input = readInput(subpackage, "input")
//    val input = readInput(subpackage, "test_input")
    val elapsed = measureTimeMillis {
        val result1 = part1(input)
        println("Part 1: $result1")
    }
    println("Time elapsed: ${elapsed / 1000.0 } seconds")
}