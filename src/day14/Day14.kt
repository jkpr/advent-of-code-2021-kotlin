package day14

import utils.readInput


fun nextGen(bigramCounts: Map<String, Long>, rules: Map<String, String>) = mutableMapOf<String, Long>().withDefault { 0 }.apply {
    bigramCounts.forEach { (bigram, count) ->
        val middle = rules.getValue(bigram)
        val first = "${bigram[0]}$middle"
        val second = "$middle${bigram[1]}"
        put(first, getValue(first) + count)
        put(second, getValue(second) + count)
    }
}

fun charCounts(bigramCounts: Map<String, Long>, first: Char, last: Char) = mutableMapOf<Char, Long>().withDefault { 0 }.apply {
    bigramCounts.entries.forEach { (bigram, count) ->
        put(bigram[0], getValue(bigram[0]) + count)
        put(bigram[1], getValue(bigram[1]) + count)
    }
    put(first, getValue(first) + 1)
    put(last, getValue(last) + 1)
}.mapValues { it.value / 2 }

fun simulate(input: List<String>, times: Int): Long {
    val start = input[0]
    var bigramCounts = start.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    val rules = input.drop(2).map { it.split(" -> ") }.associate { it[0] to it[1] }
    repeat(times) {
        bigramCounts = nextGen(bigramCounts, rules)
    }
    return charCounts(bigramCounts, start.first(), start.last()).values.let { counts -> counts.maxOf { it } - counts.minOf { it } }
}

fun part1(input: List<String>) = simulate(input, 10)

fun part2(input: List<String>) = simulate(input, 40)

fun main() {
    val subpackage = "day14"
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
