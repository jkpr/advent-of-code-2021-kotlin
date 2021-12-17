package day01

import utils.readInput

fun part1(input: List<String>) = input.map { it.toInt() }.windowed(size=2).count { it[1] > it[0] }

fun part2(input: List<String>) = input.map { it.toInt() }.windowed(size=4).count { it.takeLast(3).sum() > it.take(3).sum() }

fun main() {
    val subpackage = "day01"
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