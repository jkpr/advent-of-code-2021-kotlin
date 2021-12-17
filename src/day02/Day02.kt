package day02

import utils.readInput

fun part1(input: List<String>): Int {
    val (length, depth) = input.map { it.split(" ") }.partition { it[0] == "forward" }
    val totalLength = length.sumOf { it[1].toInt() }
    val totalDepth = depth.sumOf { if (it[0] == "down") it[1].toInt() else -it[1].toInt() }
    return totalLength * totalDepth
}

fun part2(input: List<String>): Int {
    var aim = 0
    var depth = 0
    var length = 0
    input.map { it.split(" ") }.forEach {
        val amount = it[1].toInt()
        when (it[0]) {
            "down" -> aim += amount
            "up" -> aim -= amount
            "forward" -> {
                depth += aim * amount
                length += amount
            }
        }
    }
    return length * depth
}

fun main() {
    val subpackage = "day02"
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