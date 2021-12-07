package day07

import readInput
import kotlin.math.abs


fun moveCrabs(input: List<String>, fuelCost: (distance: Int) -> Int): Int {
    val crabs = input[0].split(",").map { it.toInt() }
    return (0..(crabs.maxOf { it })).minOf { pos -> crabs.sumOf { fuelCost(abs(it - pos)) }}
}

fun part1(input: List<String>) = moveCrabs(input) { it }

fun part2(input: List<String>) = moveCrabs(input) { it * (it + 1) / 2 }

fun main() {
    val subpackage = "day07"
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
