package day06

import utils.readInput


fun spawnLanternfish(input: List<String>, days: Int): Long {
    var fish = input[0].split(",").map { it.toInt() }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()
    repeat(days) { _ ->
        val nextGen = fish.filterKeys { it != 0 }.mapKeys { it.key - 1 }.toMutableMap()
        nextGen[6] = nextGen.getOrDefault(6, 0) + fish.getOrDefault(0, 0)
        nextGen[8] = fish.getOrDefault(0, 0)
        fish = nextGen
    }
    return fish.values.sum()
}

fun part1(input: List<String>) = spawnLanternfish(input, 80)

fun part2(input: List<String>) = spawnLanternfish(input, 256)

fun main() {
    val subpackage = "day06"
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
