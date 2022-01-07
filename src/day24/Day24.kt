package day24

import kotlin.system.measureTimeMillis
import utils.readInput

fun String.lastToInt() = split(" ").last().toInt()

fun decipher(input: List<String>, maximize: Boolean) = input.chunked(18).let {
    // use a stack and build constraints
    val stack = mutableListOf<Pair<Int, Int>>()
    buildList {
        it.withIndex().forEach { (index, chunk) ->
            val push = chunk[4].lastToInt() == 1
            if (push) stack.add(index to chunk[15].lastToInt()) else {
                val (lastIndex, lastValue) = stack.removeLast()
                val diff = chunk[5].lastToInt() + lastValue
                // constraint: index = lastIndex + diff
                add(Triple(index, lastIndex, diff))
            }
        }
    }
}.let {
    // set digits based on constraints
    MutableList(14) { 0 }.apply {
        if (maximize) {
            it.forEach { (index1, index2, diff) ->
                if (diff >= 0) {
                    set(index1, 9)
                    set(index2, 9 - diff)
                } else {
                    set(index1, 9 + diff)
                    set(index2, 9)
                }
            }
        } else {
            it.forEach { (index1, index2, diff) ->
                if (diff >= 0) {
                    set(index1, 1 + diff)
                    set(index2, 1)
                } else {
                    set(index1, 1)
                    set(index2, 1 - diff)
                }
            }
        }
    }
}.joinToString("")

fun part1(input: List<String>) = decipher(input, true)

fun part2(input: List<String>) = decipher(input, false)

fun main() {
    val subpackage = "day24"
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
