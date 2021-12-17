package day03

import utils.readInput

fun part1(input: List<String>): Int {
    val gamma = buildString {
        for (i in input[0].indices) {
            append(input.map{ it[i] }.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: "")
        }
    }
    val epsilon = gamma.map{ if (it == '1') '0' else '1' }.joinToString("")
    return gamma.toInt(2) * epsilon.toInt(2)
}

fun List<String>.filterByBit(index: Int, compare: (Int, Int) -> Boolean): List<String> {
    val counts = this.map { it[index] }.groupingBy { it }.eachCount()
    if (counts.size <= 1) {  // filtering will not reduce the list
        return this
    }
    return if (compare(counts.getOrDefault('1', 0), counts.getOrDefault('0', 0))) {
        this.filter { it[index] == '1' }
    } else {
        this.filter { it[index] == '0' }
    }
}

fun part2(input: List<String>): Int {
    var oxygen = input
    var co2 = input
    for (i in input[0].indices) {
        oxygen = oxygen.filterByBit(i) { a, b -> a >= b }  // filter on the one that is greater or equal
        co2 = co2.filterByBit(i) { a, b -> a < b }  // filter on the one that is less
    }
    return oxygen[0].toInt(2) * co2[0].toInt(2)
}

fun main() {
    val subpackage = "day03"
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